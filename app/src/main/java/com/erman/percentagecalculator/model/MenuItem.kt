package com.erman.percentagecalculator.model

import com.erman.percentagecalculator.presentation.navigation.Operation

sealed class MenuItem {
    data class OperationMenuItem(
        val operation: Operation,
        val title: String,
        val description: String,
        val contentDescription: String? = title,
        val iconResId: Int
    ) : MenuItem()

    object Advertisement : MenuItem()
}
