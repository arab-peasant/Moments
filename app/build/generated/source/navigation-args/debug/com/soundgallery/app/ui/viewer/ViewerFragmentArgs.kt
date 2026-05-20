package com.soundgallery.app.ui.viewer

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmStatic

public data class ViewerFragmentArgs(
  public val photoUri: String,
  public val startPosition: Int = 0,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("photoUri", this.photoUri)
    result.putInt("startPosition", this.startPosition)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("photoUri", this.photoUri)
    result.set("startPosition", this.startPosition)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): ViewerFragmentArgs {
      bundle.setClassLoader(ViewerFragmentArgs::class.java.classLoader)
      val __photoUri : String?
      if (bundle.containsKey("photoUri")) {
        __photoUri = bundle.getString("photoUri")
        if (__photoUri == null) {
          throw IllegalArgumentException("Argument \"photoUri\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"photoUri\" is missing and does not have an android:defaultValue")
      }
      val __startPosition : Int
      if (bundle.containsKey("startPosition")) {
        __startPosition = bundle.getInt("startPosition")
      } else {
        __startPosition = 0
      }
      return ViewerFragmentArgs(__photoUri, __startPosition)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): ViewerFragmentArgs {
      val __photoUri : String?
      if (savedStateHandle.contains("photoUri")) {
        __photoUri = savedStateHandle["photoUri"]
        if (__photoUri == null) {
          throw IllegalArgumentException("Argument \"photoUri\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"photoUri\" is missing and does not have an android:defaultValue")
      }
      val __startPosition : Int?
      if (savedStateHandle.contains("startPosition")) {
        __startPosition = savedStateHandle["startPosition"]
        if (__startPosition == null) {
          throw IllegalArgumentException("Argument \"startPosition\" of type integer does not support null values")
        }
      } else {
        __startPosition = 0
      }
      return ViewerFragmentArgs(__photoUri, __startPosition)
    }
  }
}
