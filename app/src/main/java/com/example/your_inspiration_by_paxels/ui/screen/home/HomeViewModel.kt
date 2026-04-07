package com.example.your_inspiration_by_paxels.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.your_inspiration_by_paxels.data.model.Photo
import com.example.your_inspiration_by_paxels.data.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PhotoRepository) : ViewModel() {
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos = _photos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    private var currentPage = 1

    init {
        // Pantau data curated dari repository secara terus-menerus
        viewModelScope.launch {
            repository.getPhotos().collect { curatedPhotos ->
                if (_selectedCategory.value == "All") {
                    _photos.value = curatedPhotos
                }
            }
        }
        fetchPhotos()
    }

    fun setCategory(category: String) {
        // Jika klik kategori yang sama (selain All), abaikan
        if (_selectedCategory.value == category) return
        
        _selectedCategory.value = category
        currentPage = 1
        
        // Reset list lokal agar memberikan feedback visual (loading)
        _photos.value = emptyList()
        
        fetchPhotos()
    }

    fun fetchPhotos() {
        if (_isLoading.value) return
        
        viewModelScope.launch {
            _isLoading.value = true
            val category = _selectedCategory.value
            
            if (category == "All") {
                // Memanggil API curated
                val results = repository.fetchCuratedPhotos(currentPage)
                if (currentPage == 1) {
                    _photos.value = results
                } else {
                    _photos.value = _photos.value + results
                }
                currentPage++
            } else {
                // Memanggil API search berdasarkan kategori
                val results = repository.searchRemotePhotos(category, currentPage)
                if (currentPage == 1) {
                    _photos.value = results
                } else {
                    _photos.value = _photos.value + results
                }
                currentPage++
            }
            
            _isLoading.value = false
        }
    }
}
