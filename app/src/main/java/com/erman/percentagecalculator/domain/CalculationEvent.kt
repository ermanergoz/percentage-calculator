package com.erman.percentagecalculator.domain

sealed class CalculationEvent {
    data class CalculatePercentage(val val1: Double, val val2: Double) : CalculationEvent()
    data class PercentageOfValue(val val1: Double, val val2: Double) : CalculationEvent()
    data class IncreasePercentage(val val1: Double, val val2: Double) : CalculationEvent()
    data class DecreasePercentage(val val1: Double, val val2: Double) : CalculationEvent()
    data class PercentageChange(val val1: Double, val val2: Double) : CalculationEvent()
    data class FractionToPercentage(val val1: Double, val val2: Double) : CalculationEvent()

    data class Done(val result: Double) : CalculationEvent()
    data class Error(val message: String) : CalculationEvent()
}
