package com.soundgallery.app.ui.viewer

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soundgallery.app.data.model.PhotoEntry
import com.soundgallery.app.databinding.ItemViewerPhotoBinding

class ViewerPagerAdapter : ListAdapter<PhotoEntry, ViewerPagerAdapter.PhotoVH>(Differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        val binding = ItemViewerPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoVH(binding)
    }

    override fun onBindViewHolder(holder: PhotoVH, position: Int) = holder.bind(getItem(position))

    class PhotoVH(private val binding: ItemViewerPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoEntry) {
            Glide.with(binding.ivPhoto)
                .load(Uri.parse(photo.uri))
                .into(binding.ivPhoto)
        }
    }

    companion object Differ : DiffUtil.ItemCallback<PhotoEntry>() {
        override fun areItemsTheSame(a: PhotoEntry, b: PhotoEntry) = a.mediaStoreId == b.mediaStoreId
        override fun areContentsTheSame(a: PhotoEntry, b: PhotoEntry) = a == b
    }
}
