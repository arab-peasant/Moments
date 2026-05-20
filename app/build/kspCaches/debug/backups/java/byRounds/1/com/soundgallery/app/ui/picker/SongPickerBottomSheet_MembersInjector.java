package com.soundgallery.app.ui.picker;

import com.soundgallery.app.data.repository.MediaRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class SongPickerBottomSheet_MembersInjector implements MembersInjector<SongPickerBottomSheet> {
  private final Provider<MediaRepository> repoProvider;

  public SongPickerBottomSheet_MembersInjector(Provider<MediaRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  public static MembersInjector<SongPickerBottomSheet> create(
      Provider<MediaRepository> repoProvider) {
    return new SongPickerBottomSheet_MembersInjector(repoProvider);
  }

  @Override
  public void injectMembers(SongPickerBottomSheet instance) {
    injectRepo(instance, repoProvider.get());
  }

  @InjectedFieldSignature("com.soundgallery.app.ui.picker.SongPickerBottomSheet.repo")
  public static void injectRepo(SongPickerBottomSheet instance, MediaRepository repo) {
    instance.repo = repo;
  }
}
