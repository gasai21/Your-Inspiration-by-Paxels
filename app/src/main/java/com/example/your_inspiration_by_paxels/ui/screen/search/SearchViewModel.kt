package com.example.your_inspiration_by_paxels.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.your_inspiration_by_paxels.data.model.Photo
import com.example.your_inspiration_by_paxels.data.repository.PhotoRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(private val repository: PhotoRepository) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _searchResult = MutableStateFlow<List<Photo>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var currentPage = 1
    private var isEndReached = false

    init {
        // Setup search logic with debounce
        viewModelScope.launch {
            _query
                .debounce(500)
                .distinctUntilChanged()
                .collect { q ->
                    resetPagination()
                    if (q.isNotBlank()) {
                        searchPhotos(q)
                    } else {
                        fetchInitialPhotos()
                    }
                }
        }
    }

    private fun resetPagination() {
        currentPage = 1
        isEndReached = false
        _searchResult.value = emptyList()
    }

    private fun fetchInitialPhotos() {
        if (_isLoading.value || isEndReached) return
        
        viewModelScope.launch {
            _isLoading.value = true
            // Pexels curated API also supports pagination
            repository.fetchCuratedPhotos(currentPage)
            // Note: PhotoRepository.fetchCuratedPhotos updates its internal _photos state flow
            // which HomeViewModel observes. For SearchViewModel, we might want to 
            // separate curated and search or just observe the repository's curated flow.
            // However, the current Repository implementation appends to its internal _photos.
            // To make Search independent, let's just use the repo's current curated photos if query is empty.
            repository.getPhotos().first().let { 
                _searchResult.value = it
            }
            currentPage++
            _isLoading.value = false
        }
    }

    fun loadNextPage() {
        if (_isLoading.value || isEndReached) return
        
        val currentQuery = _query.value
        if (currentQuery.isNotBlank()) {
            searchPhotos(currentQuery)
        } else {
            // For empty query, we are showing curated.
            // But fetchCuratedPhotos in repo appends to _photos.
            // We should just fetch more.
            viewModelScope.launch {
                _isLoading.value = true
                repository.fetchCuratedPhotos(currentPage)
                repository.getPhotos().first().let {
                    _searchResult.value = it
                }
                currentPage++
                _isLoading.value = false
            }
        }
    }

    private fun searchPhotos(q: String) {
        if (_isLoading.value || isEndReached) return

        viewModelScope.launch {
            _isLoading.value = true
            val results = repository.searchRemotePhotos(q, currentPage)
            if (results.isEmpty()) {
                isEndReached = true
            } else {
                _searchResult.value = _searchResult.value + results
                currentPage++
            }
            _isLoading.value = false
        }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }
}
