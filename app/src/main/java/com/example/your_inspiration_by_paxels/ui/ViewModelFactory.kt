package com.example.your_inspiration_by_paxels.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.your_inspiration_by_paxels.data.repository.PhotoRepository
import com.example.your_inspiration_by_paxels.ui.screen.detail.DetailViewModel
import com.example.your_inspiration_by_paxels.ui.screen.favorite.FavoriteViewModel
import com.example.your_inspiration_by_paxels.ui.screen.home.HomeViewModel
import com.example.your_inspiration_by_paxels.ui.screen.search.SearchViewModel
import com.example.your_inspiration_by_paxels.ui.screen.setting.SettingViewModel

class ViewModelFactory(private val repository: PhotoRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
