package com.soundgallery.app.ui.picker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.soundgallery.app.data.model.AudioTrack
import com.soundgallery.app.data.repository.MediaRepository
import com.soundgallery.app.databinding.BottomSheetSongPickerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SongPickerBottomSheet : BottomSheetDialogFragment() {

    @Inject lateinit var repo: MediaRepository

    var onSongSelected: ((AudioTrack) -> Unit)? = null

    private var _binding: BottomSheetSongPickerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SongPickerAdapter
    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetSongPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SongPickerAdapter { track ->
            onSongSelected?.invoke(track)
            dismiss()
        }
        binding.rvSongs.adapter = adapter

        // Search
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(300)
                    loadSongs(s.toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        loadSongs("")
    }

    private fun loadSongs(query: String) {
        lifecycleScope.launch {
            val tracks = repo.getAudioTracks(query)
            adapter.submitList(tracks)
            binding.tvEmpty.visibility = if (tracks.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SongPickerBottomSheet()
    }
}
