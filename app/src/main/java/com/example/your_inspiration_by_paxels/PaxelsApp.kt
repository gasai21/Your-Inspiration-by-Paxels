package com.example.your_inspiration_by_paxels

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.your_inspiration_by_paxels.data.repository.PhotoRepository
import com.example.your_inspiration_by_paxels.ui.ViewModelFactory
import com.example.your_inspiration_by_paxels.ui.navigation.Screen
import com.example.your_inspiration_by_paxels.ui.navigation.navigationItems
import com.example.your_inspiration_by_paxels.ui.screen.detail.DetailScreen
import com.example.your_inspiration_by_paxels.ui.screen.detail.DetailViewModel
import com.example.your_inspiration_by_paxels.ui.screen.favorite.FavoriteScreen
import com.example.your_inspiration_by_paxels.ui.screen.favorite.FavoriteViewModel
import com.example.your_inspiration_by_paxels.ui.screen.home.HomeScreen
import com.example.your_inspiration_by_paxels.ui.screen.home.HomeViewModel
import com.example.your_inspiration_by_paxels.ui.screen.search.SearchScreen
import com.example.your_inspiration_by_paxels.ui.screen.search.SearchViewModel
import com.example.your_inspiration_by_paxels.ui.screen.setting.SettingScreen
import com.example.your_inspiration_by_paxels.ui.screen.setting.SettingViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PaxelsApp(
    navController: NavHostController = rememberNavController(),
    settingViewModel: SettingViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val repository = remember { PhotoRepository() }
    val factory = remember { ViewModelFactory(repository) }

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Detail.route) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val viewModel: HomeViewModel = viewModel(factory = factory)
                HomeScreen(
                    viewModel = viewModel,
                    navigateToDetail = { photoId ->
                        navController.navigate(Screen.Detail.createRoute(photoId))
                    },
                    navigateToSearch = {
                        navController.navigate(Screen.Search.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.Search.route) {
                val viewModel: SearchViewModel = viewModel(factory = factory)
                SearchScreen(
                    viewModel = viewModel,
                    navigateToDetail = { photoId ->
                        navController.navigate(Screen.Detail.createRoute(photoId))
                    }
                )
            }
            composable(Screen.Favorite.route) {
                val viewModel: FavoriteViewModel = viewModel(factory = factory)
                FavoriteScreen(
                    viewModel = viewModel,
                    navigateToDetail = { photoId ->
                        navController.navigate(Screen.Detail.createRoute(photoId))
                    }
                )
            }
            composable(Screen.Setting.route) {
                SettingScreen(viewModel = settingViewModel)
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("photoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("photoId") ?: -1
                val viewModel: DetailViewModel = viewModel(factory = factory)
                DetailScreen(
                    photoId = id,
                    viewModel = viewModel,
                    navigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navigationItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}
