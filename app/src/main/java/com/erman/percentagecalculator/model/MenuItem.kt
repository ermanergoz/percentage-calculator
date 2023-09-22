package com.erman.percentagecalculator.model

import com.erman.percentagecalculator.presentation.navigation.ScreenRoute

class MenuItem(
    val route: ScreenRoute,
    val title: String,
    val description: String,
    val contentDescription: String? = title,
    val iconResId: Int
)
