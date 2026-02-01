package com.erman.percentagecalculator.presentation.navigation

import com.erman.percentagecalculator.HISTORY_SCREEN_ROUTE
import com.erman.percentagecalculator.HOME_SCREEN_ROUTE
import com.erman.percentagecalculator.INPUT_SEPARATOR
import com.erman.percentagecalculator.NAV_ARGUMENT_INPUTS
import com.erman.percentagecalculator.NAV_ARGUMENT_NAME
import com.erman.percentagecalculator.domain.model.Operation

private const val CALCULATION_SCREEN_ROUTE: String = "calculation_screen"
private const val TIP_CALCULATOR_SCREEN_ROUTE: String = "tip_calculator_screen"
private const val COMPOUND_INTEREST_SCREEN_ROUTE: String = "compound_interest_screen"
private const val SETTINGS_SCREEN_ROUTE: String = "settings_screen"
private const val ABOUT_SCREEN_ROUTE: String = "about_screen"

sealed class NavigationDestination(val route: String) {
    data object HomeScreen : NavigationDestination(HOME_SCREEN_ROUTE)

    data object CalculationScreen : NavigationDestination(CALCULATION_SCREEN_ROUTE) {
        val routeWithArgs = "$route/{$NAV_ARGUMENT_NAME}?$NAV_ARGUMENT_INPUTS={$NAV_ARGUMENT_INPUTS}"

        fun createRoute(
            operation: Operation,
            inputs: List<String>? = null,
        ): String {
            val base = "$route/${operation.name}"
            return if (!inputs.isNullOrEmpty()) {
                "$base?$NAV_ARGUMENT_INPUTS=${inputs.joinToString(INPUT_SEPARATOR)}"
            } else {
                base
            }
        }
    }

    data object HistoryScreen : NavigationDestination(HISTORY_SCREEN_ROUTE)

    data object TipCalculatorScreen : NavigationDestination(TIP_CALCULATOR_SCREEN_ROUTE) {
        val routeWithArgs = "$route?$NAV_ARGUMENT_INPUTS={$NAV_ARGUMENT_INPUTS}"

        fun createRoute(inputs: List<String>? = null): String {
            return if (!inputs.isNullOrEmpty()) {
                "$route?$NAV_ARGUMENT_INPUTS=${inputs.joinToString(INPUT_SEPARATOR)}"
            } else {
                route
            }
        }
    }

    data object CompoundInterestScreen : NavigationDestination(COMPOUND_INTEREST_SCREEN_ROUTE) {
        val routeWithArgs = "$route?$NAV_ARGUMENT_INPUTS={$NAV_ARGUMENT_INPUTS}"

        fun createRoute(inputs: List<String>? = null): String {
            return if (!inputs.isNullOrEmpty()) {
                "$route?$NAV_ARGUMENT_INPUTS=${inputs.joinToString(INPUT_SEPARATOR)}"
            } else {
                route
            }
        }
    }

    data object SettingsScreen : NavigationDestination(SETTINGS_SCREEN_ROUTE)

    data object AboutScreen : NavigationDestination(ABOUT_SCREEN_ROUTE)
}
