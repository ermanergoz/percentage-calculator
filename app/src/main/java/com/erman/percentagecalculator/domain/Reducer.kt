package com.erman.percentagecalculator.domain

import androidx.lifecycle.MutableLiveData
import com.erman.percentagecalculator.RESULT_ERROR
import com.erman.percentagecalculator.UNKNOWN_ERROR
import com.erman.percentagecalculator.architecture.common.Depending
import com.erman.percentagecalculator.architecture.reducers.Reducing
import com.erman.percentagecalculator.services.ServiceLocator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("TooGenericExceptionCaught")
class Reducer : Reducing<CalculationState, CalculationEvent>, Depending<Reducer.Dependencies> {
    inner class Dependencies {
        val calculationService = ServiceLocator.calculationService
    }

    override suspend fun reduce(
        state: MutableLiveData<CalculationState>,
        event: CalculationEvent
    ): CalculationEvent? = withContext(Dispatchers.IO) {

        return@withContext when (event) {
            is CalculationEvent.Done -> {
                null
            }
            is CalculationEvent.Error -> {
                state.value?.let { appState ->
                    state.postValue(appState.copy(result = RESULT_ERROR, error = event.message))
                }
                null
            }
            else -> {
                state.value?.let { appState ->
                    try {
                        state.postValue(appState.copy(result = getResult(event), error = null))
                        CalculationEvent.Done(appState.result)
                    } catch (err: Exception) {
                        CalculationEvent.Error(err.message ?: UNKNOWN_ERROR)
                    }
                }
            }
        }
    }

    private fun getResult(event: CalculationEvent): Double {
        return when (event) {
            is CalculationEvent.DecreasePercentage -> {
                dependencies.calculationService.decreaseByPercentage(event.val1, event.val2)
            }
            is CalculationEvent.CalculatePercentage -> {
                dependencies.calculationService.findPercentage(event.val1, event.val2)
            }
            is CalculationEvent.FractionToPercentage -> {
                dependencies.calculationService.fractionToPercentage(event.val1, event.val2)
            }
            is CalculationEvent.IncreasePercentage -> {
                dependencies.calculationService.increaseByPercentage(event.val1, event.val2)
            }
            is CalculationEvent.PercentageChange -> {
                dependencies.calculationService.percentageChange(event.val1, event.val2)
            }
            is CalculationEvent.PercentageOfValue -> {
                dependencies.calculationService.findPercentageOfValue(event.val1, event.val2)
            }
            else -> {
                RESULT_ERROR
            }
        }
    }

    override val dependencies: Dependencies
        get() = Dependencies()
}
