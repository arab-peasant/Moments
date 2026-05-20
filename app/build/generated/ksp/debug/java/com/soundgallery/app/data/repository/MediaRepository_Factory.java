package com.soundgallery.app.data.repository;

import android.content.Context;
import com.soundgallery.app.data.db.PhotoDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class MediaRepository_Factory implements Factory<MediaRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<PhotoDao> photoDaoProvider;

  public MediaRepository_Factory(Provider<Context> contextProvider,
      Provider<PhotoDao> photoDaoProvider) {
    this.contextProvider = contextProvider;
    this.photoDaoProvider = photoDaoProvider;
  }

  @Override
  public MediaRepository get() {
    return newInstance(contextProvider.get(), photoDaoProvider.get());
  }

  public static MediaRepository_Factory create(Provider<Context> contextProvider,
      Provider<PhotoDao> photoDaoProvider) {
    return new MediaRepository_Factory(contextProvider, photoDaoProvider);
  }

  public static MediaRepository newInstance(Context context, PhotoDao photoDao) {
    return new MediaRepository(context, photoDao);
  }
}
