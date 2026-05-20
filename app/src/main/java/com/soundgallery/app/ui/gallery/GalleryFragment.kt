package com.soundgallery.app.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.soundgallery.app.R
import com.soundgallery.app.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var adapter: GalleryAdapter

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val granted = results.values.all { it }
        if (granted) viewModel.onPermissionGranted()
        else binding.permissionBanner.isVisible = true
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        android.util.Log.d("GalleryFrag", "pickMedia result: $uri")
        if (uri != null) {
            viewModel.onPhotoPicked(uri)
        } else {
            android.util.Log.w("GalleryFrag", "No media selected")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
        observeEvents()
        checkPermissions()

        binding.btnGrantPermission.setOnClickListener { requestPermissions() }
        binding.swipeRefresh.setOnRefreshListener { viewModel.refreshGallery() }
        binding.fabAdd.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }


    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is GalleryEvent.ScrollToTop -> {
                            android.util.Log.d("GalleryFrag", "Scrolling to top")
                            binding.rvGallery.scrollToPosition(0)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = GalleryAdapter(
            onPhotoClick = { photo, position ->
                val action = GalleryFragmentDirections.actionGalleryToViewer(
                    photoUri = photo.uri,
                    startPosition = position
                )
                findNavController().navigate(action)
            },
            onPhotoLongClick = { photo ->
                showDeleteDialog(photo)
            }
        )

        binding.rvGallery.apply {
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (this@GalleryFragment.adapter.getItemViewType(position) == 0) 2 else 1
                }
            }
            layoutManager = gridLayoutManager
            adapter = this@GalleryFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun showDeleteDialog(photo: com.soundgallery.app.data.model.PhotoEntry) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Remove Photo")
            .setMessage("Do you want to remove this photo from your gallery?")
            .setPositiveButton("Remove") { _, _ ->
                viewModel.removePhoto(photo)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.swipeRefresh.isRefreshing = state.isLoading
                    binding.permissionBanner.isVisible = !state.hasPermission && !state.isLoading
                    binding.emptyState.isVisible = state.items.isEmpty() && !state.isLoading && state.hasPermission
                    adapter.submitList(state.items)
                }
            }
        }
    }

    private fun checkPermissions() {
        val perms = requiredPermissions()
        val allGranted = perms.all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
        if (allGranted) viewModel.onPermissionGranted()
        else requestPermissions()
    }

    private fun requestPermissions() {
        permissionLauncher.launch(requiredPermissions().toTypedArray())
    }

    private fun requiredPermissions(): List<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
