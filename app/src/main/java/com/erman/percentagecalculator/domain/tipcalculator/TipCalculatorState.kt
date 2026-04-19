package com.erman.percentagecalculator.domain.tipcalculator

data class TipCalculatorState(
    val billAmount: String = "",
    val tipPercentage: String = "",
    val splitCount: String = "1",
    val tipAmount: Double? = null,
    val totalAmount: Double? = null,
    val perPersonAmount: Double? = null,
    val error: String? = null,
)
