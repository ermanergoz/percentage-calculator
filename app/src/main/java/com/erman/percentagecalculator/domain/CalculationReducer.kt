package com.erman.percentagecalculator.domain

import com.erman.percentagecalculator.architecture.reducers.Reducer

class CalculationReducer : Reducer<CalculationState, CalculationEvent> {
    override fun reduce(
        state: CalculationState,
        event: CalculationEvent,
    ): CalculationState {
        return when (event) {
            is CalculationEvent.UpdateFirstInput -> state.copy(firstInput = event.value)
            is CalculationEvent.UpdateSecondInput -> state.copy(secondInput = event.value)
            is CalculationEvent.CalculationComplete ->
                state.copy(
                    result = event.result,
                    secondaryResult = event.secondaryResult,
                    error = null,
                )
            is CalculationEvent.CalculationFailed -> state.copy(result = null, error = event.message)
            is CalculationEvent.CalculatePercentage,
            is CalculationEvent.PercentageOfValue,
            is CalculationEvent.IncreasePercentage,
            is CalculationEvent.DecreasePercentage,
            is CalculationEvent.PercentageChange,
            is CalculationEvent.FractionToPercentage,
            is CalculationEvent.CalculateDiscount,
            is CalculationEvent.CalculateMarkup,
            is CalculationEvent.CalculateTax,
            is CalculationEvent.CalculateGpa,
            -> state
        }
    }
}
