package com.example.earningstracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.earningstracker.ui.screens.HistoryScreen
import com.example.earningstracker.ui.screens.HomeScreen

/**
 * Navigation destinations
 */
object Destinations {
    const val HOME_ROUTE = "home"
    const val HISTORY_ROUTE = "history"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.HOME_ROUTE
    ) {
        composable(Destinations.HOME_ROUTE) {
            HomeScreen(
                onNavigateToHistory = {
                    navController.navigate(Destinations.HISTORY_ROUTE)
                }
            )
        }
        
        composable(Destinations.HISTORY_ROUTE) {
            HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 