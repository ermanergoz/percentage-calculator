package com.erman.percentagecalculator.presentation.navigation

import com.erman.percentagecalculator.CALCULATION_SCREEN_ROUTE
import com.erman.percentagecalculator.HOME_SCREEN_ROUTE

sealed class NavigationDestination(val route: String) {
    object HomeScreen : NavigationDestination(HOME_SCREEN_ROUTE)
    object CalculationScreen : NavigationDestination(CALCULATION_SCREEN_ROUTE)
}
