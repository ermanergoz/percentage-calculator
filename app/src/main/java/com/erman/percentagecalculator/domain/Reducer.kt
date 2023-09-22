package com.erman.percentagecalculator.domain

import androidx.lifecycle.MutableLiveData
import com.erman.percentagecalculator.architecture.reducers.Reducing

@Suppress("TooGenericExceptionCaught")
// TODO: Simplify this whole thing and create a model class to store the operations and either use
//  dependency interface or data injection
class Reducer : Reducing<AppState, CalculationEvent> {
    override suspend fun reduce(
        state: MutableLiveData<AppState>,
        event: CalculationEvent
    ): CalculationEvent? {
        return when (event) {
            is CalculationEvent.DecreasePercentage -> TODO()
            is CalculationEvent.FindPercentage -> {
                state.value?.let { appState ->
                    try {
                        state.postValue(appState.copy(result = event.val1 * event.val2, error = null))
                        CalculationEvent.Done(appState.result)
                    } catch (err: Exception) {
                        CalculationEvent.Error(err.message ?: "-")
                    }
                }
            }
            is CalculationEvent.FractionToPercentage -> TODO()
            is CalculationEvent.IncreasePercentage -> TODO()
            is CalculationEvent.Margin -> TODO()
            is CalculationEvent.PercentageChange -> TODO()
            is CalculationEvent.PercentageOfValue -> TODO()
            is CalculationEvent.PriceComparison -> TODO()
            is CalculationEvent.TipSplit -> TODO()
            is CalculationEvent.Error -> {
                state.value?.let { appState ->
                    state.postValue(appState.copy(result = -1.0, error = event.message))
                }
                null
            }
            else -> null
        }
    }
}
