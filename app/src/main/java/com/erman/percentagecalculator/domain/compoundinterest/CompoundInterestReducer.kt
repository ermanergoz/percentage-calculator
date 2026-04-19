package com.erman.percentagecalculator.domain.compoundinterest

import com.erman.percentagecalculator.architecture.reducers.Reducer

class CompoundInterestReducer : Reducer<CompoundInterestState, CompoundInterestEvent> {
    override fun reduce(
        state: CompoundInterestState,
        event: CompoundInterestEvent,
    ): CompoundInterestState {
        return when (event) {
            is CompoundInterestEvent.UpdatePrincipal -> state.copy(principal = event.value)
            is CompoundInterestEvent.UpdateAnnualRate -> state.copy(annualRate = event.value)
            is CompoundInterestEvent.UpdateTime -> state.copy(timeInYears = event.value)
            is CompoundInterestEvent.UpdateCompoundsPerYear -> state.copy(compoundsPerYear = event.value)
            is CompoundInterestEvent.Calculate -> state
            is CompoundInterestEvent.Clear -> CompoundInterestState()
            is CompoundInterestEvent.CalculationComplete ->
                state.copy(
                    futureValue = event.futureValue,
                    totalInterest = event.totalInterest,
                    error = null,
                )
            is CompoundInterestEvent.CalculationFailed ->
                state.copy(futureValue = null, totalInterest = null, error = event.message)
        }
    }
}
