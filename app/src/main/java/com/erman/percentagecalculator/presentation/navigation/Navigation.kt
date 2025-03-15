package com.erman.percentagecalculator.presentation.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.erman.percentagecalculator.NAV_ARGUMENT_NAME
import com.erman.percentagecalculator.NAV_ARGUMENT_PLACEHOLDER
import com.erman.percentagecalculator.SEPARATOR
import com.erman.percentagecalculator.presentation.screeens.CalculatePercentageScreen
import com.erman.percentagecalculator.presentation.screeens.HomeScreen
import com.erman.percentagecalculator.presentation.viewModel.PercentageCalculatorViewModel

@Composable
fun Navigation(viewModel: PercentageCalculatorViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationDestination.HomeScreen.route) {
        composable(route = NavigationDestination.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = NavigationDestination.CalculationScreen.route + SEPARATOR + NAV_ARGUMENT_PLACEHOLDER,
            arguments = listOf(
                navArgument(NAV_ARGUMENT_NAME) {
                    type = NavType.EnumType(Operation::class.java)
                }
            )
        ) { navBackStackEntry ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                navBackStackEntry.arguments?.getSerializable(NAV_ARGUMENT_NAME, Operation::class.java)
            } else {
                @Suppress("DEPRECATION")
                navBackStackEntry.arguments?.getSerializable(NAV_ARGUMENT_NAME) as Operation
            }?.let { operation ->
                CalculatePercentageScreen(navController = navController, viewModel = viewModel, operation = operation)
            }
        }
    }
}
