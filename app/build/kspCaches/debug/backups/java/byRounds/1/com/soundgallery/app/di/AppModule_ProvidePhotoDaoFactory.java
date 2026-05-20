package com.soundgallery.app.di;

import com.soundgallery.app.data.db.PhotoDao;
import com.soundgallery.app.data.db.SoundGalleryDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AppModule_ProvidePhotoDaoFactory implements Factory<PhotoDao> {
  private final Provider<SoundGalleryDatabase> dbProvider;

  public AppModule_ProvidePhotoDaoFactory(Provider<SoundGalleryDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PhotoDao get() {
    return providePhotoDao(dbProvider.get());
  }

  public static AppModule_ProvidePhotoDaoFactory create(Provider<SoundGalleryDatabase> dbProvider) {
    return new AppModule_ProvidePhotoDaoFactory(dbProvider);
  }

  public static PhotoDao providePhotoDao(SoundGalleryDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePhotoDao(db));
  }
}
