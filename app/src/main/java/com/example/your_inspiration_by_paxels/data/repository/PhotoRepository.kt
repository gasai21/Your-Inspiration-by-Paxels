package com.example.your_inspiration_by_paxels.data.repository

import com.example.your_inspiration_by_paxels.data.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class PhotoRepository {
    private val _photos = MutableStateFlow(
        listOf(
            Photo(1, "https://images.pexels.com/photos/167699/pexels-photo-167699.jpeg", "Sasha Prasastika", "Nature"),
            Photo(2, "https://images.pexels.com/photos/235986/pexels-photo-235986.jpeg", "Pixabay", "Stars"),
            Photo(3, "https://images.pexels.com/photos/1133957/pexels-photo-1133957.jpeg", "Philipp Maiwald", "City"),
            Photo(4, "https://images.pexels.com/photos/459225/pexels-photo-459225.jpeg", "Pixabay", "Forest"),
            Photo(5, "https://images.pexels.com/photos/1323550/pexels-photo-1323550.jpeg", "Simon Matzinger", "Mountain"),
            Photo(6, "https://images.pexels.com/photos/326055/pexels-photo-326055.jpeg", "Pixabay", "Butterfly")
        )
    )

    fun getPhotos(): Flow<List<Photo>> = _photos

    fun getPhotoById(id: Int): Flow<Photo?> = _photos.map { photos ->
        photos.find { it.id == id }
    }

    fun searchPhotos(query: String): Flow<List<Photo>> = _photos.map { photos ->
        photos.filter { it.photographer.contains(query, ignoreCase = true) || it.alt.contains(query, ignoreCase = true) }
    }

    fun getFavoritePhotos(): Flow<List<Photo>> = _photos.map { photos ->
        photos.filter { it.isFavorite }
    }

    fun toggleFavorite(photoId: Int) {
        val currentList = _photos.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == photoId }
        if (index != -1) {
            val photo = currentList[index]
            currentList[index] = photo.copy(isFavorite = !photo.isFavorite)
            _photos.value = currentList
        }
    }
}
