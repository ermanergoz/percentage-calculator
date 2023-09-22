package com.erman.percentagecalculator.domain

sealed class CalculationEvent {
    data class FindPercentage(val val1: Double, val val2: Double) : CalculationEvent()
    data class PercentageOfValue(val val1: Int, val val2: Int) : CalculationEvent()
    data class IncreasePercentage(val val1: Int, val val2: Int) : CalculationEvent()
    data class DecreasePercentage(val val1: Int, val val2: Int) : CalculationEvent()
    data class PercentageChange(val val1: Int, val val2: Int) : CalculationEvent()
    data class Margin(val val1: Int, val val2: Int) : CalculationEvent()
    data class FractionToPercentage(val val1: Int, val val2: Int) : CalculationEvent()
    data class TipSplit(val val1: Int, val val2: Int) : CalculationEvent()
    data class PriceComparison(val val1: Int, val val2: Int) : CalculationEvent()

    data class Done(val result: Double) : CalculationEvent()
    data class Error(val message: String) : CalculationEvent()
}
