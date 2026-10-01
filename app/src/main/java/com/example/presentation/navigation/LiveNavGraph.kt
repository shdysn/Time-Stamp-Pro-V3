package com.example.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.presentation.screen.FacebookLiveScreen
import com.example.presentation.screen.HomeScreen
import com.example.presentation.screen.LiveControlScreen
import com.example.presentation.screen.LiveHistoryScreen
import com.example.presentation.screen.LoginScreen
import com.example.presentation.screen.ScreenSharingPermissionScreen
import com.example.presentation.screen.SettingsScreen
import com.example.presentation.screen.SplashScreen
import com.example.presentation.screen.YouTubeLiveScreen
import com.example.presentation.viewmodel.LiveViewModel

@Composable
fun LiveNavGraph(
    navController: NavHostController,
    viewModel: LiveViewModel
) {
    val isLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        // 1. Splash Screen
        composable(Routes.SPLASH) {
            SplashScreen(
                isLoggedIn = isLoggedIn,
                onNavigateNext = { destination ->
                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // 2. Login Screen
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 3. Home Screen
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateFacebookLive = { navController.navigate(Routes.FACEBOOK_LIVE) },
                onNavigateYouTubeLive = { navController.navigate(Routes.YOUTUBE_LIVE) },
                onNavigateHistory = { navController.navigate(Routes.LIVE_HISTORY) },
                onNavigateSettings = { navController.navigate(Routes.SETTINGS) },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // 4. Facebook Live Screen
        composable(Routes.FACEBOOK_LIVE) {
            FacebookLiveScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onStartCameraLive = { navController.navigate(Routes.LIVE_CONTROL) },
                onStartScreenLive = { navController.navigate(Routes.SCREEN_PERMISSION) }
            )
        }

        // 5. YouTube Live Screen
        composable(Routes.YOUTUBE_LIVE) {
            YouTubeLiveScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onStartCameraLive = { navController.navigate(Routes.LIVE_CONTROL) },
                onStartScreenLive = { navController.navigate(Routes.SCREEN_PERMISSION) }
            )
        }

        // 6. Screen Sharing Permission Screen
        composable(Routes.SCREEN_PERMISSION) {
            ScreenSharingPermissionScreen(
                viewModel = viewModel,
                onStartNow = {
                    navController.navigate(Routes.LIVE_CONTROL) {
                        popUpTo(Routes.SCREEN_PERMISSION) { inclusive = true }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }

        // 7. Live Control Screen
        composable(Routes.LIVE_CONTROL) {
            LiveControlScreen(
                viewModel = viewModel,
                onStopLiveCompleted = {
                    navController.navigate(Routes.LIVE_HISTORY) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                }
            )
        }

        // 8. Live History Screen
        composable(Routes.LIVE_HISTORY) {
            LiveHistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 9. Settings Screen
        composable(Routes.SETTINGS) {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
