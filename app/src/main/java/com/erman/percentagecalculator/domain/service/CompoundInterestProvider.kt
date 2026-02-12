package com.erman.percentagecalculator.domain.service

import com.erman.percentagecalculator.PERCENTAGE_ENTIRETY
import com.erman.percentagecalculator.domain.model.CompoundInterestResult
import kotlin.math.pow

class CompoundInterestProvider : CompoundInterestService {
    override fun calculate(
        principal: Double,
        annualRate: Double,
        timeInYears: Double,
        compoundsPerYear: Int,
    ): CompoundInterestResult {
        require(compoundsPerYear > 0) { "Compounds per year must be at least 1" }
        val rate = annualRate / PERCENTAGE_ENTIRETY
        val futureValue =
            principal * (1 + rate / compoundsPerYear).pow(compoundsPerYear * timeInYears)
        val totalInterest = futureValue - principal
        return CompoundInterestResult(
            futureValue = futureValue,
            totalInterest = totalInterest,
        )
    }
}
