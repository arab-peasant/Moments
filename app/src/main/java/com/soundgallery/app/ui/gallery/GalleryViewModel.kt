package com.soundgallery.app.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundgallery.app.data.model.PhotoEntry
import com.soundgallery.app.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class GalleryTab { ALL, LIKED }

sealed class GalleryEvent {
    object ScrollToTop : GalleryEvent()
}

sealed class GalleryItem {
    data class Photo(val photo: PhotoEntry) : GalleryItem()
    data class Header(val year: Int) : GalleryItem()
}

data class GalleryUiState(
    val items: List<GalleryItem> = emptyList(),
    val photos: List<PhotoEntry> = emptyList(), // Keep original list for navigation/indexing
    val isLoading: Boolean = true,
    val hasPermission: Boolean = false,
    val activeTab: GalleryTab = GalleryTab.ALL
)

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repo: MediaRepository
) : ViewModel() {

    private val _tab = MutableStateFlow(GalleryTab.ALL)
    private val _hasPermission = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)

    private val _events = MutableSharedFlow<GalleryEvent>()
    val events = _events.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<GalleryUiState> = combine(
        _tab,
        _hasPermission,
        _isLoading
    ) { tab, hasPermission, loading ->
        Triple(tab, hasPermission, loading)
    }.flatMapLatest { (tab, hasPermission, loading) ->
        val photoFlow = when (tab) {
            GalleryTab.ALL -> repo.getPhotos()
            GalleryTab.LIKED -> repo.getLikedPhotos()
        }
        photoFlow.map { photos ->
            GalleryUiState(
                items = insertYearHeaders(photos),
                photos = photos,
                isLoading = loading,
                hasPermission = hasPermission,
                activeTab = tab
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GalleryUiState())

    private fun insertYearHeaders(photos: List<PhotoEntry>): List<GalleryItem> {
        if (photos.isEmpty()) return emptyList()
        val items = mutableListOf<GalleryItem>()
        val calendar = java.util.Calendar.getInstance()
        
        var currentYear = -1
        
        photos.forEach { photo ->
            val date = photo.customDate ?: photo.dateTaken
            calendar.timeInMillis = date
            val year = calendar.get(java.util.Calendar.YEAR)
            
            if (year != currentYear) {
                items.add(GalleryItem.Header(year))
                currentYear = year
            }
            items.add(GalleryItem.Photo(photo))
        }
        return items
    }

    fun onPermissionGranted() {
        _hasPermission.value = true
        _isLoading.value = false // Skip initial sync since we only want manually added photos
    }

    fun setTab(tab: GalleryTab) {
        _tab.value = tab
    }

    private suspend fun runSync() {
        _isLoading.value = true
        repo.syncPhotos()
        _isLoading.value = false
    }

    private fun syncPhotos() {
        viewModelScope.launch {
            runSync()
        }
    }

    fun refreshGallery() = syncPhotos()

    fun removePhoto(photo: PhotoEntry) {
        viewModelScope.launch {
            repo.removePhotoFromGallery(photo.uri)
        }
    }

    fun onPhotoPicked(uri: android.net.Uri?) {
        android.util.Log.d("GalleryVM", "onPhotoPicked: $uri")
        uri ?: return
        viewModelScope.launch {
            _isLoading.value = true
            repo.addPhotoByUri(uri)
            android.util.Log.d("GalleryVM", "Photo added, scrolling")
            _isLoading.value = false
            _events.emit(GalleryEvent.ScrollToTop)
        }
    }
}
