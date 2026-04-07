package com.example.your_inspiration_by_paxels.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)

val navigationItems = listOf(
    NavigationItem("Home", Icons.Default.Home, Screen.Home),
    NavigationItem("Search", Icons.Default.Search, Screen.Search),
    NavigationItem("Favorite", Icons.Default.Favorite, Screen.Favorite),
    NavigationItem("Setting", Icons.Default.Settings, Screen.Setting)
)
