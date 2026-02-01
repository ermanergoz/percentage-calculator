package com.erman.percentagecalculator.domain

data class CalculationState(
    val firstInput: String = "",
    val secondInput: String = "",
    val result: Double? = null,
    val secondaryResult: Double? = null,
    val error: String? = null,
)
