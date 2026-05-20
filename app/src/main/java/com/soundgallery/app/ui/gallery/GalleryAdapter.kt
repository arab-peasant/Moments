package com.soundgallery.app.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.soundgallery.app.R
import com.soundgallery.app.data.model.PhotoEntry
import com.soundgallery.app.databinding.ItemGalleryHeaderBinding
import com.soundgallery.app.databinding.ItemPhotoBinding

class GalleryAdapter(
    private val onPhotoClick: (PhotoEntry, Int) -> Unit,
    private val onPhotoLongClick: (PhotoEntry) -> Unit
) : ListAdapter<GalleryItem, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GalleryItem.Photo -> VIEW_TYPE_PHOTO
            is GalleryItem.Header -> VIEW_TYPE_HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemGalleryHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemPhotoBinding.inflate(inflater, parent, false)
                PhotoViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is GalleryItem.Header -> (holder as HeaderViewHolder).bind(item.year)
            is GalleryItem.Photo -> {
                // To maintain the correct index for navigation, we need to know the photo's index in the full list.
                // However, for simplicity here, we'll pass the list index. 
                // The Fragment should handle the mapping if needed.
                (holder as PhotoViewHolder).bind(item.photo)
            }
        }
    }

    inner class HeaderViewHolder(private val binding: ItemGalleryHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(year: Int) {
            binding.tvHeaderYear.text = year.toString()
        }
    }

    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoEntry) {
            Glide.with(binding.ivPhoto)
                .load(photo.uri)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(150))
                .into(binding.ivPhoto)

            val hasSong = photo.linkedSongId != null
            binding.musicBadge.alpha = if (hasSong) 1f else 0f
            if (hasSong) {
                binding.tvSongTitle.text = photo.linkedSongTitle ?: "Unknown"
            }

            binding.ivLiked.alpha = if (photo.isLiked) 1f else 0f

            val dateMillis = photo.customDate ?: photo.dateTaken
            binding.tvDate.text = android.text.format.DateFormat.getMediumDateFormat(binding.root.context).format(dateMillis)

            binding.root.setOnClickListener {
                // Find index in original photo list
                val items = currentList
                val photosOnly = items.filterIsInstance<GalleryItem.Photo>().map { it.photo }
                val photoIndex = photosOnly.indexOf(photo)
                onPhotoClick(photo, photoIndex)
            }

            binding.root.setOnLongClickListener {
                onPhotoLongClick(photo)
                true
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_PHOTO = 1

        private val DiffCallback = object : DiffUtil.ItemCallback<GalleryItem>() {
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
                return when {
                    oldItem is GalleryItem.Photo && newItem is GalleryItem.Photo ->
                        oldItem.photo.uri == newItem.photo.uri
                    oldItem is GalleryItem.Header && newItem is GalleryItem.Header ->
                        oldItem.year == newItem.year
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
