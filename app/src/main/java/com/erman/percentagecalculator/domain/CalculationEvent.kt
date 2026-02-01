package com.erman.percentagecalculator.domain

sealed class CalculationEvent {
    data class UpdateFirstInput(val value: String) : CalculationEvent()

    data class UpdateSecondInput(val value: String) : CalculationEvent()

    data class CalculatePercentage(val part: Double, val whole: Double) : CalculationEvent()

    data class PercentageOfValue(val percentage: Double, val value: Double) : CalculationEvent()

    data class IncreasePercentage(val value: Double, val percentage: Double) : CalculationEvent()

    data class DecreasePercentage(val value: Double, val percentage: Double) : CalculationEvent()

    data class PercentageChange(val oldValue: Double, val newValue: Double) : CalculationEvent()

    data class FractionToPercentage(val numerator: Double, val denominator: Double) : CalculationEvent()

    data class CalculateDiscount(val originalPrice: Double, val discountPercent: Double) : CalculationEvent()

    data class CalculateMarkup(val cost: Double, val markupPercent: Double) : CalculationEvent()

    data class CalculateTax(val amount: Double, val taxRate: Double) : CalculationEvent()

    data class CalculateGpa(val percentage: Double) : CalculationEvent()

    data class CalculationComplete(val result: Double, val secondaryResult: Double? = null) : CalculationEvent()

    data class CalculationFailed(val message: String) : CalculationEvent()
}
