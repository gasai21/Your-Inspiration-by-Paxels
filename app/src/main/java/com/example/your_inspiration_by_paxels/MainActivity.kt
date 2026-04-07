package com.example.your_inspiration_by_paxels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.your_inspiration_by_paxels.data.repository.PhotoRepository
import com.example.your_inspiration_by_paxels.ui.ViewModelFactory
import com.example.your_inspiration_by_paxels.ui.screen.setting.SettingViewModel
import com.example.your_inspiration_by_paxels.ui.theme.YourinspirationbypaxelsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = PhotoRepository()
            val factory = ViewModelFactory(repository)
            val settingViewModel: SettingViewModel = viewModel(factory = factory)
            val isDarkMode by settingViewModel.isDarkMode.collectAsState()

            YourinspirationbypaxelsTheme(darkTheme = isDarkMode) {
                PaxelsApp(settingViewModel = settingViewModel)
            }
        }
    }
}
