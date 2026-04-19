package com.erman.percentagecalculator.domain.compoundinterest

data class CompoundInterestState(
    val principal: String = "",
    val annualRate: String = "",
    val timeInYears: String = "",
    val compoundsPerYear: String = "12",
    val futureValue: Double? = null,
    val totalInterest: Double? = null,
    val error: String? = null,
)
