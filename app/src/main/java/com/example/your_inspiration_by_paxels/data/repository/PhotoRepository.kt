package com.example.your_inspiration_by_paxels.data.repository

import com.example.your_inspiration_by_paxels.BuildConfig
import com.example.your_inspiration_by_paxels.data.model.Photo
import com.example.your_inspiration_by_paxels.data.remote.PexelsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotoRepository {
    private val apiKey = BuildConfig.PEXELS_API_KEY

    private val apiService = Retrofit.Builder()
        .baseUrl("https://api.pexels.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PexelsApiService::class.java)

    // Flow untuk data curated (untuk Home "All" dan Search awal)
    private val _curatedPhotos = MutableStateFlow<List<Photo>>(emptyList())
    
    // Cache untuk SEMUA foto yang pernah di-load agar DetailScreen bisa menemukannya
    private val _allPhotosCache = MutableStateFlow<Map<Int, Photo>>(emptyMap())

    private fun updateCache(photos: List<Photo>) {
        _allPhotosCache.update { currentCache ->
            currentCache + photos.associateBy { it.id }
        }
    }

    suspend fun fetchCuratedPhotos(page: Int): List<Photo> {
        if (apiKey.isEmpty()) return emptyList()
        return try {
            val response = apiService.getCuratedPhotos(apiKey, page = page, perPage = 20)
            val newPhotos = response.photos.map { remote ->
                Photo(
                    id = remote.id,
                    url = remote.src.large2x,
                    photographer = remote.photographer,
                    alt = remote.alt
                )
            }
            
            updateCache(newPhotos)
            
            if (page == 1) {
                _curatedPhotos.value = newPhotos
            } else {
                _curatedPhotos.value = _curatedPhotos.value + newPhotos
            }
            newPhotos
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchRemotePhotos(query: String, page: Int): List<Photo> {
        if (apiKey.isEmpty()) return emptyList()
        return try {
            val response = apiService.searchPhotos(apiKey, query, page = page, perPage = 20)
            val newPhotos = response.photos.map { remote ->
                Photo(
                    id = remote.id,
                    url = remote.src.large2x,
                    photographer = remote.photographer,
                    alt = remote.alt
                )
            }
            
            updateCache(newPhotos)
            newPhotos
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getPhotos(): Flow<List<Photo>> = _curatedPhotos

    fun getPhotoById(id: Int): Flow<Photo?> = _allPhotosCache.map { it[id] }

    fun getFavoritePhotos(): Flow<List<Photo>> = _allPhotosCache.map { cache ->
        cache.values.filter { it.isFavorite }
    }

    fun toggleFavorite(photoId: Int) {
        _allPhotosCache.update { currentCache ->
            val photo = currentCache[photoId]
            if (photo != null) {
                currentCache + (photoId to photo.copy(isFavorite = !photo.isFavorite))
            } else {
                currentCache
            }
        }
        
        // Juga update di list curated jika ada
        val currentCurated = _curatedPhotos.value.toMutableList()
        val index = currentCurated.indexOfFirst { it.id == photoId }
        if (index != -1) {
            currentCurated[index] = currentCurated[index].copy(isFavorite = !currentCurated[index].isFavorite)
            _curatedPhotos.value = currentCurated
        }
    }
}
