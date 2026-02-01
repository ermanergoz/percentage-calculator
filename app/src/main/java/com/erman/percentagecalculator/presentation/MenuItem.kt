package com.erman.percentagecalculator.presentation

import com.erman.percentagecalculator.domain.model.Operation

sealed class MenuItem {
    data class OperationItem(val operation: Operation) : MenuItem()

    data class Advertisement(val adUnitIdRes: Int) : MenuItem()
}
