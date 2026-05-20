package com.soundgallery.app.ui.gallery;

import com.soundgallery.app.data.repository.MediaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class GalleryViewModel_Factory implements Factory<GalleryViewModel> {
  private final Provider<MediaRepository> repoProvider;

  public GalleryViewModel_Factory(Provider<MediaRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public GalleryViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static GalleryViewModel_Factory create(Provider<MediaRepository> repoProvider) {
    return new GalleryViewModel_Factory(repoProvider);
  }

  public static GalleryViewModel newInstance(MediaRepository repo) {
    return new GalleryViewModel(repo);
  }
}
