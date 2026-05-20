package com.soundgallery.app.ui.gallery

import android.os.Bundle
import androidx.navigation.NavDirections
import com.soundgallery.app.R
import kotlin.Int
import kotlin.String

public class GalleryFragmentDirections private constructor() {
  private data class ActionGalleryToViewer(
    public val photoUri: String,
    public val startPosition: Int = 0,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_gallery_to_viewer

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("photoUri", this.photoUri)
        result.putInt("startPosition", this.startPosition)
        return result
      }
  }

  public companion object {
    public fun actionGalleryToViewer(photoUri: String, startPosition: Int = 0): NavDirections =
        ActionGalleryToViewer(photoUri, startPosition)
  }
}
