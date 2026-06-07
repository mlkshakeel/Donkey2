package com.example

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*

@Composable
fun DonkeyRunApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_menu",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("main_menu") {
            MainMenuScreen(
                onNavigateToShop = { navController.navigate("shop") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToGame = { navController.navigate("in_game") }
            )
        }
        composable("shop") {
            ShopScreen(
                onNavigateMenu = { navController.navigate("main_menu") { popUpTo("main_menu") { inclusive = false } } },
                onNavigateSettings = { navController.navigate("settings") { popUpTo("main_menu") } },
                onNavigatePlay = { navController.navigate("in_game") }
            )
        }
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable("in_game") {
            InGameHudScreen(
                onCrash = { navController.navigate("game_over") }
            )
        }
        composable("game_over") {
            GameOverScreen(
                onRevive = { navController.navigateUp() }, // Go back to game
                onMenu = { navController.navigate("main_menu") { popUpTo("main_menu") { inclusive = true } } },
                onQuit = { navController.navigate("main_menu") { popUpTo("main_menu") { inclusive = true } } }
            )
        }
    }
}
