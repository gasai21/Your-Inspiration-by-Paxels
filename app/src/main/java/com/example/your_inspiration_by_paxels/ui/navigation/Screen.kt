package com.example.your_inspiration_by_paxels.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Detail : Screen("detail/{photoId}") {
        fun createRoute(photoId: Int) = "detail/$photoId"
    }
    object Favorite : Screen("favorite")
    object Setting : Screen("setting")
}
