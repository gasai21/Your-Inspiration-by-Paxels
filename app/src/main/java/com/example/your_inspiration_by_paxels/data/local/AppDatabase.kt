package com.example.your_inspiration_by_paxels.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.your_inspiration_by_paxels.data.local.dao.FavoritePhotoDao
import com.example.your_inspiration_by_paxels.data.local.entity.FavoritePhotoEntity

@Database(entities = [FavoritePhotoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePhotoDao(): FavoritePhotoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "paxels_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
