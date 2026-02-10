package com.erman.percentagecalculator.domain.tipcalculator

import com.erman.percentagecalculator.architecture.reducers.Reducer

class TipCalculatorReducer : Reducer<TipCalculatorState, TipCalculatorEvent> {
    override fun reduce(
        state: TipCalculatorState,
        event: TipCalculatorEvent,
    ): TipCalculatorState {
        return when (event) {
            is TipCalculatorEvent.UpdateBillAmount -> state.copy(billAmount = event.value)
            is TipCalculatorEvent.UpdateTipPercentage -> state.copy(tipPercentage = event.value)
            is TipCalculatorEvent.UpdateSplitCount -> state.copy(splitCount = event.value)
            is TipCalculatorEvent.Calculate -> state
            is TipCalculatorEvent.Clear -> TipCalculatorState()
            is TipCalculatorEvent.CalculationComplete ->
                state.copy(
                    tipAmount = event.tipAmount,
                    totalAmount = event.totalAmount,
                    perPersonAmount = event.perPersonAmount,
                    error = null,
                )
            is TipCalculatorEvent.CalculationFailed ->
                state.copy(
                    tipAmount = null,
                    totalAmount = null,
                    perPersonAmount = null,
                    error = event.message,
                )
        }
    }
}
