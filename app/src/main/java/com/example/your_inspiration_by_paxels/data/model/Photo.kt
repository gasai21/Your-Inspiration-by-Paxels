package com.example.your_inspiration_by_paxels.data.model

data class Photo(
    val id: Int,
    val url: String,
    val photographer: String,
    val alt: String,
    val isFavorite: Boolean = false
)
