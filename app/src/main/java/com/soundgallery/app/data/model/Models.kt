package com.soundgallery.app.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Represents a photo from the device gallery, with an optional linked song.
 */
@Parcelize
@Entity(tableName = "photo_entries")
data class PhotoEntry(
    @PrimaryKey val uri: String,
    val mediaStoreId: Long,
    val displayName: String,
    val dateTaken: Long,
    val linkedSongId: Long? = null,       // MediaStore audio ID
    val linkedSongUri: String? = null,
    val linkedSongTitle: String? = null,
    val linkedSongArtist: String? = null,
    val linkedSongAlbum: String? = null,
    val linkedSongDuration: Long? = null,  // ms
    val caption: String? = null,
    val isLiked: Boolean = false,
    val isManuallyAdded: Boolean = false,
    val customDate: Long? = null,
    val lastPlaybackPosition: Long = 0L
) : Parcelable

/**
 * A device audio track from MediaStore.
 */
@Parcelize
data class AudioTrack(
    val id: Long,
    val uri: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,       // ms
    val albumArtUri: String?
) : Parcelable {
    val durationFormatted: String
        get() {
            val totalSeconds = duration / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return "%d:%02d".format(minutes, seconds)
        }
}
