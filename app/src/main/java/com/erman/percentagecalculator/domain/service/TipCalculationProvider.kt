package com.erman.percentagecalculator.domain.service

import com.erman.percentagecalculator.PERCENTAGE_ENTIRETY
import com.erman.percentagecalculator.domain.model.TipCalculatorResult

class TipCalculationProvider : TipCalculationService {
    override fun calculate(
        billAmount: Double,
        tipPercentage: Double,
        splitCount: Int,
    ): TipCalculatorResult {
        require(splitCount > 0) { "Split count must be at least 1" }
        val tipAmount = billAmount * (tipPercentage / PERCENTAGE_ENTIRETY)
        val totalAmount = billAmount + tipAmount
        val perPersonAmount = totalAmount / splitCount
        return TipCalculatorResult(
            tipAmount = tipAmount,
            totalAmount = totalAmount,
            perPersonAmount = perPersonAmount,
        )
    }
}
