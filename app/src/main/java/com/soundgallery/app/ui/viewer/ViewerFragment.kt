package com.soundgallery.app.ui.viewer

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.soundgallery.app.R
import com.soundgallery.app.databinding.FragmentViewerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewerFragment : Fragment() {

    private var _binding: FragmentViewerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ViewerViewModel by viewModels()
    private val args: ViewerFragmentArgs by navArgs()
    private lateinit var pagerAdapter: ViewerPagerAdapter

    private val pickAudio = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.linkSongUri(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPager()
        setupControls()
        observeState()
    }

    private fun setupPager() {
        pagerAdapter = ViewerPagerAdapter()
        binding.viewPager.apply {
            adapter = pagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            
            // Add zoom-out page transformer for smoother feel
            setPageTransformer { page, position ->
                val absPos = Math.abs(position)
                page.apply {
                    val scale = if (absPos > 1) 0.85f else 1 - (absPos * 0.15f)
                    scaleX = scale
                    scaleY = scale
                    alpha = if (absPos > 1) 0.5f else 1 - (absPos * 0.5f)
                }
            }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.onPageChanged(position)
                }
            })
        }
    }

    private fun setupControls() {
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnPlayPause.setOnClickListener { viewModel.togglePlayPause() }
        binding.btnAttachSongInline.setOnClickListener { showSongPicker() }
        binding.btnSaveMusic.setOnClickListener {
            viewModel.saveLinkedMusic()
            android.widget.Toast.makeText(requireContext(), "Music saved", android.widget.Toast.LENGTH_SHORT).show()
        }
        binding.btnSaveDescription.setOnClickListener {
            val description = binding.etDescription.text.toString()
            viewModel.saveCaption(description)
            android.widget.Toast.makeText(requireContext(), "Story saved", android.widget.Toast.LENGTH_SHORT).show()
            
            // Exit edit mode
            binding.editModeContainer.isVisible = false
            binding.tvStoryDisplay.isVisible = true

            // Hide keyboard
            val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(binding.etDescription.windowToken, 0)
        }

        binding.tvStoryDisplay.setOnClickListener {
            binding.tvStoryDisplay.isVisible = false
            binding.editModeContainer.isVisible = true
            binding.etDescription.requestFocus()
            // Show keyboard
            val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imm.showSoftInput(binding.etDescription, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
        }

        binding.tvStoryDate.setOnClickListener {
            showDatePicker()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: android.widget.SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.seekTo(progress / 1000f)
            }
            override fun onStartTrackingTouch(sb: android.widget.SeekBar) {}
            override fun onStopTrackingTouch(sb: android.widget.SeekBar) {}
        })
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (pagerAdapter.itemCount != state.photos.size) {
                        pagerAdapter.submitList(state.photos)
                        binding.viewPager.setCurrentItem(state.currentIndex, false)
                    }

                    val photo = state.currentPhoto
                    if (photo != null) applyDynamicBackground(Uri.parse(photo.uri))

                    val hasSong = state.hasSong
                    binding.playerCard.isVisible = hasSong
                    binding.noSongCard.isVisible = !hasSong

                    if (hasSong && (photo != null || state.pendingAudioUri != null)) {
                        binding.tvSongTitle.text = state.pendingAudioTitle ?: photo?.linkedSongTitle ?: "Unknown"
                        binding.tvSongArtist.text = state.pendingAudioArtist ?: photo?.linkedSongArtist ?: "Unknown Artist"
                        
                        binding.btnSaveMusic.isVisible = state.pendingAudioUri != null
                    }

                    binding.btnPlayPause.setImageResource(
                        if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    )
                    binding.waveformView.isPlaying = state.isPlaying

                    if (state.durationMs > 0) {
                        binding.seekBar.max = 1000
                        binding.seekBar.progress = (state.progressFraction * 1000).toInt()
                        binding.tvTimeElapsed.text = formatTime(state.progressMs)
                        binding.tvTimeDuration.text = formatTime(state.durationMs)
                    }

                    val caption = photo?.caption
                    
                    // Always update display text
                    binding.tvStoryDisplay.text = if (caption.isNullOrBlank()) {
                        "Tap to write the story behind this moment..."
                    } else {
                        caption
                    }

                    // Update description EditText only if it's not focused and in edit mode
                    if (!binding.etDescription.hasFocus() && binding.editModeContainer.isVisible) {
                        binding.etDescription.setText(caption ?: "")
                    } else if (!binding.editModeContainer.isVisible) {
                        // Keep EditText synced while in view mode
                        binding.etDescription.setText(caption ?: "")
                    }

                    // Update Date
                    val dateMillis = photo?.customDate ?: System.currentTimeMillis()
                    binding.tvStoryDate.text = android.text.format.DateFormat.getMediumDateFormat(requireContext()).format(dateMillis)
                }
            }
        }
    }

    private fun showDatePicker() {
        val state = viewModel.uiState.value
        val currentMillis = state.currentPhoto?.customDate ?: System.currentTimeMillis()
        
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = currentMillis
        }

        android.app.DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val newCalendar = java.util.Calendar.getInstance().apply {
                    set(java.util.Calendar.YEAR, year)
                    set(java.util.Calendar.MONTH, month)
                    set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                viewModel.saveDate(newCalendar.timeInMillis)
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun applyDynamicBackground(uri: Uri) {
        // Load blurred background
        Glide.with(this)
            .load(uri)
            .override(20, 20) // Very small for "natural" blur when scaled up
            .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade())
            .into(binding.ivBackgroundBlur)

        // Palette for dynamic tinting
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .override(200, 200)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>,
                    isFirst: Boolean
                ): Boolean = false

                override fun onResourceReady(
                    resource: Bitmap,
                    model: Any,
                    target: Target<Bitmap>?,
                    dataSource: DataSource,
                    isFirst: Boolean
                ): Boolean {
                    Palette.from(resource).generate { palette ->
                        val color = palette?.getDarkVibrantColor(
                            palette.getMutedColor(0xFF1A1825.toInt())
                        ) ?: 0xFF1A1825.toInt()
                        binding.backgroundOverlay.setBackgroundColor(color)
                    }
                    return false
                }
            })
            .preload()
    }

    private fun showSongPicker() {
        pickAudio.launch("audio/*")
    }

    private fun formatTime(ms: Long): String {
        val totalSec = ms / 1000
        return "%d:%02d".format(totalSec / 60, totalSec % 60)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
