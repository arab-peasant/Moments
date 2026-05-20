package com.soundgallery.app.ui.viewer

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.soundgallery.app.data.model.AudioTrack
import com.soundgallery.app.data.model.PhotoEntry
import com.soundgallery.app.data.repository.MediaRepository
import com.soundgallery.app.service.MusicPlaybackService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ViewerUiState(
    val photos: List<PhotoEntry> = emptyList(),
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false,
    val progressMs: Long = 0L,
    val durationMs: Long = 0L,
    val isLiked: Boolean = false,
    val pendingAudioUri: Uri? = null,
    val pendingAudioTitle: String? = null,
    val pendingAudioArtist: String? = null
) {
    val currentPhoto: PhotoEntry? get() = photos.getOrNull(currentIndex)
    val hasSong: Boolean get() = currentPhoto?.linkedSongId != null || pendingAudioUri != null
    val progressFraction: Float get() = if (durationMs > 0) progressMs.toFloat() / durationMs else 0f
}

@HiltViewModel
class ViewerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: MediaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val startPhotoUri: String = savedStateHandle["photoUri"] ?: ""
    private val startPosition: Int = savedStateHandle["startPosition"] ?: 0

    private val _uiState = MutableStateFlow(ViewerUiState(currentIndex = startPosition))
    val uiState: StateFlow<ViewerUiState> = _uiState.asStateFlow()

    private var player: MediaController? = null

    init {
        loadPhotos()
        connectPlayer()
        startProgressTracking()
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            repo.getPhotos().collect { photos ->
                val idx = photos.indexOfFirst { it.uri == startPhotoUri }
                    .takeIf { it >= 0 } ?: startPosition
                _uiState.update { it.copy(photos = photos, currentIndex = idx) }
                // Auto-play song for current photo
                playCurrentPhotoSong()
            }
        }
    }

    private fun connectPlayer() {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, MusicPlaybackService::class.java)
        )
        val future = MediaController.Builder(context, sessionToken).buildAsync()
        future.addListener({
            player = future.get()
            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _uiState.update { it.copy(isPlaying = isPlaying) }
                }
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        _uiState.update { it.copy(progressMs = 0) }
                    }
                }
            })
            playCurrentPhotoSong()
        }, MoreExecutors.directExecutor())
    }

    private fun startProgressTracking() {
        viewModelScope.launch {
            while (true) {
                delay(1000) // Update and save position every second
                player?.let { p ->
                    val position = p.currentPosition.coerceAtLeast(0)
                    _uiState.update { state ->
                        state.copy(
                            progressMs = position,
                            durationMs = p.duration.coerceAtLeast(0)
                        )
                    }
                    // Save position to database if playing or just paused
                    _uiState.value.currentPhoto?.let { photo ->
                        if (photo.linkedSongUri != null && (p.isPlaying || position > 0)) {
                            repo.savePlaybackPosition(photo.uri, position)
                        }
                    }
                }
            }
        }
    }

    fun onPageChanged(index: Int) {
        _uiState.update { it.copy(currentIndex = index) }
        playCurrentPhotoSong()
    }

    private fun playCurrentPhotoSong() {
        val photo = _uiState.value.currentPhoto ?: return
        val p = player ?: return

        if (photo.linkedSongUri != null) {
            val mediaItem = MediaItem.Builder()
                .setUri(Uri.parse(photo.linkedSongUri))
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(photo.linkedSongTitle)
                        .setArtist(photo.linkedSongArtist)
                        .setAlbumTitle(photo.linkedSongAlbum)
                        .build()
                )
                .build()

            // Only reload if different song
            val currentUri = p.currentMediaItem?.localConfiguration?.uri?.toString()
            if (currentUri != photo.linkedSongUri) {
                p.setMediaItem(mediaItem)
                // Resume from last position
                p.prepare()
                p.seekTo(photo.lastPlaybackPosition)
                p.play()
            }
        } else {
            p.stop()
        }
    }

    fun togglePlayPause() {
        val p = player ?: return
        if (p.isPlaying) p.pause() else p.play()
    }

    fun seekTo(fraction: Float) {
        val dur = player?.duration ?: return
        player?.seekTo((dur * fraction).toLong())
    }

    fun toggleLike() {
        val photo = _uiState.value.currentPhoto ?: return
        viewModelScope.launch {
            repo.toggleLike(photo)
        }
    }

    fun linkSong(track: AudioTrack) {
        val photoUri = _uiState.value.currentPhoto?.uri ?: return
        viewModelScope.launch {
            repo.linkSong(photoUri, track)
            // Give Room a moment to update, then replay
            delay(100)
            playCurrentPhotoSong()
        }
    }

    fun linkSongUri(uri: Uri) {
        // Try to get title from MediaStore
        var title = "Unknown"
        var artist = "Unknown Artist"
        try {
            context.contentResolver.query(uri, arrayOf(android.provider.MediaStore.Audio.Media.TITLE, android.provider.MediaStore.Audio.Media.ARTIST), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    title = cursor.getString(0) ?: "Unknown"
                    artist = cursor.getString(1) ?: "Unknown Artist"
                }
            }
        } catch (e: Exception) {}

        _uiState.update { it.copy(
            pendingAudioUri = uri,
            pendingAudioTitle = title,
            pendingAudioArtist = artist
        )}

        // Play it immediately so the user can hear it
        val mediaItem = MediaItem.fromUri(uri)
        player?.let { p ->
            p.setMediaItem(mediaItem)
            p.prepare()
            p.play()
        }
    }

    fun saveLinkedMusic() {
        val uri = _uiState.value.pendingAudioUri ?: return
        val photoUri = _uiState.value.currentPhoto?.uri ?: return
        viewModelScope.launch {
            repo.linkSongByUri(photoUri, uri)
            _uiState.update { it.copy(
                pendingAudioUri = null,
                pendingAudioTitle = null,
                pendingAudioArtist = null
            )}
            // Re-sync with database to ensure everything is solid
            delay(100)
            playCurrentPhotoSong()
        }
    }

    fun unlinkSong() {
        val photoUri = _uiState.value.currentPhoto?.uri ?: return
        viewModelScope.launch {
            repo.unlinkSong(photoUri)
            player?.stop()
        }
    }

    fun saveCaption(caption: String) {
        val photoUri = _uiState.value.currentPhoto?.uri ?: return
        viewModelScope.launch { repo.saveCaption(photoUri, caption) }
    }

    fun saveDate(dateMillis: Long) {
        val photoUri = _uiState.value.currentPhoto?.uri ?: return
        viewModelScope.launch { repo.saveCustomDate(photoUri, dateMillis) }
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
    }
}
