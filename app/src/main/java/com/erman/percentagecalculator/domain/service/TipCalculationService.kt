package com.erman.percentagecalculator.domain.service

import com.erman.percentagecalculator.domain.model.TipCalculatorResult

interface TipCalculationService {
    fun calculate(
        billAmount: Double,
        tipPercentage: Double,
        splitCount: Int,
    ): TipCalculatorResult
}
