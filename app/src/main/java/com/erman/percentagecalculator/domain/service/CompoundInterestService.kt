package com.erman.percentagecalculator.domain.service

import com.erman.percentagecalculator.domain.model.CompoundInterestResult

interface CompoundInterestService {
    fun calculate(
        principal: Double,
        annualRate: Double,
        timeInYears: Double,
        compoundsPerYear: Int,
    ): CompoundInterestResult
}
