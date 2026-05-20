package com.soundgallery.app.di

import android.content.Context
import androidx.room.Room
import com.soundgallery.app.data.db.PhotoDao
import com.soundgallery.app.data.db.SoundGalleryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SoundGalleryDatabase =
        Room.databaseBuilder(
            context,
            SoundGalleryDatabase::class.java,
            "sound_gallery.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun providePhotoDao(db: SoundGalleryDatabase): PhotoDao = db.photoDao()
}
