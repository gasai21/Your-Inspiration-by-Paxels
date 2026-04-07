package com.example.your_inspiration_by_paxels.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_photos")
data class FavoritePhotoEntity(
    @PrimaryKey val id: Int,
    val url: String,
    val photographer: String,
    val alt: String
)
