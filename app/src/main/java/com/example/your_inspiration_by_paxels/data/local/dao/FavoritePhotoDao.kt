package com.example.your_inspiration_by_paxels.data.local.dao

import androidx.room.*
import com.example.your_inspiration_by_paxels.data.local.entity.FavoritePhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePhotoDao {
    @Query("SELECT * FROM favorite_photos")
    fun getAllFavoritePhotos(): Flow<List<FavoritePhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePhoto(photo: FavoritePhotoEntity)

    @Delete
    suspend fun deleteFavoritePhoto(photo: FavoritePhotoEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_photos WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}
