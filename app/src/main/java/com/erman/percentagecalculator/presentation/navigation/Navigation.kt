package com.erman.percentagecalculator.presentation.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.erman.percentagecalculator.INPUT_SEPARATOR
import com.erman.percentagecalculator.NAV_ARGUMENT_INPUTS
import com.erman.percentagecalculator.NAV_ARGUMENT_NAME
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.presentation.screens.AboutScreen
import com.erman.percentagecalculator.presentation.screens.CalculationScreen
import com.erman.percentagecalculator.presentation.screens.CompoundInterestScreen
import com.erman.percentagecalculator.presentation.screens.HistoryScreen
import com.erman.percentagecalculator.presentation.screens.HomeScreen
import com.erman.percentagecalculator.presentation.screens.SettingsScreen
import com.erman.percentagecalculator.presentation.screens.TipCalculatorScreen

@Suppress("LongMethod")
@Composable
fun Navigation(initialOperation: Operation? = null) {
    val navController = rememberNavController()

    LaunchedEffect(initialOperation) {
        if (initialOperation != null) {
            val route =
                when (initialOperation) {
                    Operation.TIP_CALCULATOR -> NavigationDestination.TipCalculatorScreen.route
                    Operation.COMPOUND_INTEREST -> NavigationDestination.CompoundInterestScreen.route
                    else -> NavigationDestination.CalculationScreen.createRoute(initialOperation)
                }
            navController.navigate(route)
        }
    }

    val navigateToHistory = {
        navController.navigate(NavigationDestination.HistoryScreen.route)
    }

    NavHost(navController = navController, startDestination = NavigationDestination.HomeScreen.route) {
        composable(route = NavigationDestination.HomeScreen.route) {
            HomeScreen(
                onOperationClick = { operation ->
                    when (operation) {
                        Operation.TIP_CALCULATOR ->
                            navController.navigate(NavigationDestination.TipCalculatorScreen.route)
                        Operation.COMPOUND_INTEREST ->
                            navController.navigate(NavigationDestination.CompoundInterestScreen.route)
                        else ->
                            navController.navigate(NavigationDestination.CalculationScreen.createRoute(operation))
                    }
                },
                onHistoryClick = navigateToHistory,
                onSettingsClick = {
                    navController.navigate(NavigationDestination.SettingsScreen.route)
                },
                onAboutClick = {
                    navController.navigate(NavigationDestination.AboutScreen.route)
                },
            )
        }
        composable(
            route = NavigationDestination.CalculationScreen.routeWithArgs,
            arguments =
                listOf(
                    navArgument(NAV_ARGUMENT_NAME) {
                        type = NavType.EnumType(Operation::class.java)
                    },
                    navArgument(NAV_ARGUMENT_INPUTS) {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    },
                ),
        ) { navBackStackEntry ->
            val operation =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    navBackStackEntry.arguments?.getSerializable(NAV_ARGUMENT_NAME, Operation::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    navBackStackEntry.arguments?.getSerializable(NAV_ARGUMENT_NAME) as? Operation
                }
            if (operation != null) {
                CalculationScreen(
                    operation = operation,
                    initialInputs =
                        navBackStackEntry.arguments?.getString(NAV_ARGUMENT_INPUTS)
                            ?.takeIf { it.isNotEmpty() }?.split(INPUT_SEPARATOR),
                    onBackClick = { navController.popBackStack() },
                    onHistoryClick = navigateToHistory,
                )
            } else {
                LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }
        composable(route = NavigationDestination.HistoryScreen.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() },
                onEntryClick = { operation, inputs ->
                    when (operation) {
                        Operation.TIP_CALCULATOR ->
                            navController.navigate(NavigationDestination.TipCalculatorScreen.createRoute(inputs))
                        Operation.COMPOUND_INTEREST ->
                            navController.navigate(
                                NavigationDestination.CompoundInterestScreen.createRoute(inputs),
                            )
                        else ->
                            navController.navigate(
                                NavigationDestination.CalculationScreen.createRoute(operation, inputs),
                            )
                    }
                },
            )
        }
        composable(
            route = NavigationDestination.TipCalculatorScreen.routeWithArgs,
            arguments =
                listOf(
                    navArgument(NAV_ARGUMENT_INPUTS) {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    },
                ),
        ) { navBackStackEntry ->
            TipCalculatorScreen(
                initialInputs =
                    navBackStackEntry.arguments?.getString(NAV_ARGUMENT_INPUTS)
                        ?.takeIf { it.isNotEmpty() }?.split(INPUT_SEPARATOR),
                onBackClick = { navController.popBackStack() },
                onHistoryClick = navigateToHistory,
            )
        }
        composable(
            route = NavigationDestination.CompoundInterestScreen.routeWithArgs,
            arguments =
                listOf(
                    navArgument(NAV_ARGUMENT_INPUTS) {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    },
                ),
        ) { navBackStackEntry ->
            CompoundInterestScreen(
                initialInputs =
                    navBackStackEntry.arguments?.getString(NAV_ARGUMENT_INPUTS)
                        ?.takeIf { it.isNotEmpty() }?.split(INPUT_SEPARATOR),
                onBackClick = { navController.popBackStack() },
                onHistoryClick = navigateToHistory,
            )
        }
        composable(route = NavigationDestination.SettingsScreen.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(route = NavigationDestination.AboutScreen.route) {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
