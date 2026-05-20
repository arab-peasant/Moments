package com.soundgallery.app.data.db

import androidx.room.*
import com.soundgallery.app.data.model.PhotoEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo_entries WHERE isManuallyAdded = 1 ORDER BY COALESCE(customDate, dateTaken) DESC")
    fun getAllPhotos(): Flow<List<PhotoEntry>>

    @Query("SELECT * FROM photo_entries WHERE uri = :uri")
    suspend fun getPhotoByUri(uri: String): PhotoEntry?

    @Query("SELECT * FROM photo_entries WHERE isLiked = 1 AND isManuallyAdded = 1 ORDER BY COALESCE(customDate, dateTaken) DESC")
    fun getLikedPhotos(): Flow<List<PhotoEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: PhotoEntry)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhotos(photos: List<PhotoEntry>)

    @Update
    suspend fun updatePhoto(photo: PhotoEntry)

    @Query("""
        UPDATE photo_entries SET
            linkedSongId = :songId,
            linkedSongUri = :songUri,
            linkedSongTitle = :title,
            linkedSongArtist = :artist,
            linkedSongAlbum = :album,
            linkedSongDuration = :duration
        WHERE uri = :photoUri
    """)
    suspend fun linkSongToPhoto(
        photoUri: String,
        songId: Long,
        songUri: String,
        title: String,
        artist: String,
        album: String,
        duration: Long
    )

    @Query("UPDATE photo_entries SET linkedSongId = NULL, linkedSongUri = NULL, linkedSongTitle = NULL, linkedSongArtist = NULL, linkedSongAlbum = NULL, linkedSongDuration = NULL WHERE uri = :photoUri")
    suspend fun unlinkSongFromPhoto(photoUri: String)

    @Query("UPDATE photo_entries SET isLiked = :liked WHERE uri = :photoUri")
    suspend fun setLiked(photoUri: String, liked: Boolean)

    @Query("UPDATE photo_entries SET caption = :caption WHERE uri = :photoUri")
    suspend fun setCaption(photoUri: String, caption: String)

    @Query("UPDATE photo_entries SET customDate = :date WHERE uri = :photoUri")
    suspend fun setCustomDate(photoUri: String, date: Long)

    @Query("UPDATE photo_entries SET lastPlaybackPosition = :position WHERE uri = :photoUri")
    suspend fun setLastPlaybackPosition(photoUri: String, position: Long)

    @Query("UPDATE photo_entries SET isManuallyAdded = 0 WHERE uri = :photoUri")
    suspend fun removePhotoFromGallery(photoUri: String)

    @Delete
    suspend fun deletePhoto(photo: PhotoEntry)
}

@Database(entities = [PhotoEntry::class], version = 5, exportSchema = false)
abstract class SoundGalleryDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}
