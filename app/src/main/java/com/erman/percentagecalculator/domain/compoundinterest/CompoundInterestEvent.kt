package com.erman.percentagecalculator.domain.compoundinterest

sealed class CompoundInterestEvent {
    data class UpdatePrincipal(val value: String) : CompoundInterestEvent()

    data class UpdateAnnualRate(val value: String) : CompoundInterestEvent()

    data class UpdateTime(val value: String) : CompoundInterestEvent()

    data class UpdateCompoundsPerYear(val value: String) : CompoundInterestEvent()

    data object Calculate : CompoundInterestEvent()

    data object Clear : CompoundInterestEvent()

    data class CalculationComplete(val futureValue: Double, val totalInterest: Double) : CompoundInterestEvent()

    data class CalculationFailed(val message: String) : CompoundInterestEvent()
}
