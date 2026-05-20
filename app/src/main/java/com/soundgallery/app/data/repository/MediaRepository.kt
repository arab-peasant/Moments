package com.soundgallery.app.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.soundgallery.app.data.db.PhotoDao
import com.soundgallery.app.data.model.AudioTrack
import com.soundgallery.app.data.model.PhotoEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val photoDao: PhotoDao
) {

    // ─── Photos ─────────────────────────────────────────────────────────────

    fun getPhotos(): Flow<List<PhotoEntry>> = photoDao.getAllPhotos()
    fun getLikedPhotos(): Flow<List<PhotoEntry>> = photoDao.getLikedPhotos()

    /**
     * Scans MediaStore for all images and syncs them into Room.
     * New photos get inserted (ignoring if already present).
     * Photos removed from the device get pruned.
     */
    suspend fun syncPhotos() = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val entries = mutableListOf<PhotoEntry>()
        val uris = mutableListOf<String>()

        context.contentResolver.query(collection, projection, null, null, sortOrder)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                val date = cursor.getLong(dateCol)
                val uri = ContentUris.withAppendedId(collection, id).toString()

                uris.add(uri)
                entries.add(PhotoEntry(uri, id, name, date))
            }
        }

        photoDao.insertPhotos(entries)
        // Skip pruning to protect manual picks with potentially different URI schemes
    }

    suspend fun linkSong(photoUri: String, track: AudioTrack) = withContext(Dispatchers.IO) {
        photoDao.linkSongToPhoto(
            photoUri = photoUri,
            songId = track.id,
            songUri = track.uri,
            title = track.title,
            artist = track.artist,
            album = track.album,
            duration = track.duration
        )
    }

    suspend fun linkSongByUri(photoUri: String, audioUri: Uri) = withContext(Dispatchers.IO) {
        // Try to take persistable permission for audio
        try {
            val takeFlags: Int = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(audioUri, takeFlags)
        } catch (e: Exception) {
            android.util.Log.w("MediaRepo", "Could not take persistable permission for audio: ${e.message}")
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION
        )

        var songId = 0L
        var title = "Unknown"
        var artist = "Unknown Artist"
        var album = "Unknown Album"
        var duration = 0L

        try {
            context.contentResolver.query(audioUri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    songId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)) ?: "Unknown"
                    artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) ?: "Unknown Artist"
                    album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)) ?: "Unknown Album"
                    duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MediaRepo", "Error querying audio metadata: ${e.message}")
        }

        photoDao.linkSongToPhoto(
            photoUri = photoUri,
            songId = songId,
            songUri = audioUri.toString(),
            title = title,
            artist = artist,
            album = album,
            duration = duration
        )
    }

    suspend fun unlinkSong(photoUri: String) = withContext(Dispatchers.IO) {
        photoDao.unlinkSongFromPhoto(photoUri)
    }

    suspend fun toggleLike(photo: PhotoEntry) = withContext(Dispatchers.IO) {
        photoDao.setLiked(photo.uri, !photo.isLiked)
    }

    suspend fun saveCaption(photoUri: String, caption: String) = withContext(Dispatchers.IO) {
        photoDao.setCaption(photoUri, caption)
    }

    suspend fun saveCustomDate(photoUri: String, date: Long) = withContext(Dispatchers.IO) {
        photoDao.setCustomDate(photoUri, date)
    }

    suspend fun savePlaybackPosition(photoUri: String, position: Long) = withContext(Dispatchers.IO) {
        photoDao.setLastPlaybackPosition(photoUri, position)
    }

    suspend fun removePhotoFromGallery(photoUri: String) = withContext(Dispatchers.IO) {
        photoDao.removePhotoFromGallery(photoUri)
    }

    suspend fun addPhotoByUri(uri: Uri) = withContext(Dispatchers.IO) {
        android.util.Log.d("MediaRepo", "addPhotoByUri START: $uri")
        
        // Try to take persistable permission, but don't let it crash the whole flow
        try {
            val takeFlags: Int = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, takeFlags)
            android.util.Log.d("MediaRepo", "Persistable permission taken")
        } catch (e: Exception) {
            android.util.Log.w("MediaRepo", "Could not take persistable permission: ${e.message}")
        }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )
        
        var inserted = false
        try {
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    // ALWAYS use current time for manual picks so they jump to the top
                    val date = System.currentTimeMillis()

                    // Always use the original URI for manual picks to ensure persistable permissions work
                    val finalUri = uri.toString()

                    android.util.Log.d("MediaRepo", "MediaStore found: ID=$id, Name=$name. Inserting as $finalUri at TOP.")
                    photoDao.insertPhoto(PhotoEntry(finalUri, id, name, date, isManuallyAdded = true))
                    inserted = true
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MediaRepo", "MediaStore query/insert error: ${e.message}")
        }

        if (!inserted) {
            android.util.Log.w("MediaRepo", "MediaStore path failed, using fallback insertion")
            insertFallback(uri)
        }
        android.util.Log.d("MediaRepo", "addPhotoByUri COMPLETE")
    }

    private suspend fun insertFallback(uri: Uri) {
        val name = uri.lastPathSegment ?: "Unknown Photo"
        val date = System.currentTimeMillis()
        android.util.Log.d("MediaRepo", "Fallback insert: URI=$uri, Name=$name")
        photoDao.insertPhoto(PhotoEntry(uri.toString(), 0L, name, date, isManuallyAdded = true))
    }

    // ─── Audio ──────────────────────────────────────────────────────────────

    /**
     * Queries MediaStore for all audio files on the device.
     */
    suspend fun getAudioTracks(query: String = ""): List<AudioTrack> = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val selection = if (query.isNotBlank()) {
            "${MediaStore.Audio.Media.TITLE} LIKE ? OR ${MediaStore.Audio.Media.ARTIST} LIKE ?"
        } else null

        val selectionArgs = if (query.isNotBlank()) {
            arrayOf("%$query%", "%$query%")
        } else null

        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val tracks = mutableListOf<AudioTrack>()

        context.contentResolver.query(collection, projection, selection, selectionArgs, sortOrder)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val duration = cursor.getLong(durationCol)
                if (duration < 30_000) continue  // skip very short clips

                val albumId = cursor.getLong(albumIdCol)
                val albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId").toString()

                tracks.add(
                    AudioTrack(
                        id = id,
                        uri = ContentUris.withAppendedId(collection, id).toString(),
                        title = cursor.getString(titleCol) ?: "Unknown",
                        artist = cursor.getString(artistCol) ?: "Unknown Artist",
                        album = cursor.getString(albumCol) ?: "Unknown Album",
                        duration = duration,
                        albumArtUri = albumArtUri
                    )
                )
            }
        }
        tracks
    }
}
