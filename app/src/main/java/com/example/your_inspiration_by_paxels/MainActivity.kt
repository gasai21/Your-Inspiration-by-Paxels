package com.example.your_inspiration_by_paxels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.your_inspiration_by_paxels.data.local.AppDatabase
import com.example.your_inspiration_by_paxels.data.repository.PhotoRepository
import com.example.your_inspiration_by_paxels.ui.ViewModelFactory
import com.example.your_inspiration_by_paxels.ui.screen.setting.SettingViewModel
import com.example.your_inspiration_by_paxels.ui.theme.YourinspirationbypaxelsTheme
import com.example.your_inspiration_by_paxels.utils.NetworkObserver

val LocalNetworkStatus = staticCompositionLocalOf { true }

class MainActivity : ComponentActivity() {
    private lateinit var networkObserver: NetworkObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkObserver = NetworkObserver(applicationContext)
        
        enableEdgeToEdge()
        setContent {
            val database = AppDatabase.getDatabase(applicationContext)
            val repository = PhotoRepository(database.favoritePhotoDao())
            val factory = ViewModelFactory(repository)
            
            val settingViewModel: SettingViewModel = viewModel(factory = factory)
            val isDarkMode by settingViewModel.isDarkMode.collectAsState()
            val isConnected by networkObserver.isConnected.collectAsState(initial = true)

            CompositionLocalProvider(LocalNetworkStatus provides isConnected) {
                YourinspirationbypaxelsTheme(darkTheme = isDarkMode) {
                    PaxelsApp(settingViewModel = settingViewModel, repository = repository)
                }
            }
        }
    }
}
