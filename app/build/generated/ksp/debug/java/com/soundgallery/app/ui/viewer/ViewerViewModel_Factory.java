package com.soundgallery.app.ui.viewer;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import com.soundgallery.app.data.repository.MediaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ViewerViewModel_Factory implements Factory<ViewerViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<MediaRepository> repoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ViewerViewModel_Factory(Provider<Context> contextProvider,
      Provider<MediaRepository> repoProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    this.contextProvider = contextProvider;
    this.repoProvider = repoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ViewerViewModel get() {
    return newInstance(contextProvider.get(), repoProvider.get(), savedStateHandleProvider.get());
  }

  public static ViewerViewModel_Factory create(Provider<Context> contextProvider,
      Provider<MediaRepository> repoProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ViewerViewModel_Factory(contextProvider, repoProvider, savedStateHandleProvider);
  }

  public static ViewerViewModel newInstance(Context context, MediaRepository repo,
      SavedStateHandle savedStateHandle) {
    return new ViewerViewModel(context, repo, savedStateHandle);
  }
}
