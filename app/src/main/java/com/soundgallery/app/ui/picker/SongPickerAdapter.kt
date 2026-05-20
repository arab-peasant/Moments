package com.soundgallery.app.ui.picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soundgallery.app.R
import com.soundgallery.app.data.model.AudioTrack
import com.soundgallery.app.databinding.ItemSongBinding

class SongPickerAdapter(
    private val onTrackSelected: (AudioTrack) -> Unit
) : ListAdapter<AudioTrack, SongPickerAdapter.SongVH>(Differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongVH {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongVH(binding)
    }

    override fun onBindViewHolder(holder: SongVH, position: Int) = holder.bind(getItem(position))

    inner class SongVH(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: AudioTrack) {
            binding.tvTitle.text = track.title
            binding.tvArtist.text = track.artist
            binding.tvDuration.text = track.durationFormatted

            Glide.with(binding.ivAlbumArt)
                .load(track.albumArtUri)
                .placeholder(R.drawable.ic_music_placeholder)
                .centerCrop()
                .into(binding.ivAlbumArt)

            binding.root.setOnClickListener { onTrackSelected(track) }
        }
    }

    companion object Differ : DiffUtil.ItemCallback<AudioTrack>() {
        override fun areItemsTheSame(a: AudioTrack, b: AudioTrack) = a.id == b.id
        override fun areContentsTheSame(a: AudioTrack, b: AudioTrack) = a == b
    }
}
