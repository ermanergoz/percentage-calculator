package com.erman.percentagecalculator.domain.tipcalculator

sealed class TipCalculatorEvent {
    data class UpdateBillAmount(val value: String) : TipCalculatorEvent()

    data class UpdateTipPercentage(val value: String) : TipCalculatorEvent()

    data class UpdateSplitCount(val value: String) : TipCalculatorEvent()

    data object Calculate : TipCalculatorEvent()

    data object Clear : TipCalculatorEvent()

    data class CalculationComplete(
        val tipAmount: Double,
        val totalAmount: Double,
        val perPersonAmount: Double,
    ) : TipCalculatorEvent()

    data class CalculationFailed(val message: String) : TipCalculatorEvent()
}
