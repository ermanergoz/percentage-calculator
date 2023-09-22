package com.erman.percentagecalculator.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erman.percentagecalculator.presentation.screeens.FindPercentageScreen
import com.erman.percentagecalculator.presentation.screeens.HomeScreen
import com.erman.percentagecalculator.presentation.viewModel.PercentageCalculatorViewModel

@Composable
fun Navigation(viewModel: PercentageCalculatorViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ScreenRoute.HOME_SCREEN.name) {
        composable(route = ScreenRoute.HOME_SCREEN.name) {
            HomeScreen(navController = navController)
        }
        composable(route = ScreenRoute.FIND_PERCENTAGE.name) {
            FindPercentageScreen(viewModel)
        }
    }
}
