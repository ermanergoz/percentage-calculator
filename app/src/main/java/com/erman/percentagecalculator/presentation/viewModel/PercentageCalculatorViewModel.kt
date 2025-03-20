package com.erman.percentagecalculator.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.erman.percentagecalculator.CHAR_TO_BE_IGNORED
import com.erman.percentagecalculator.IGNORED_CHAR_REPLACEMENT
import com.erman.percentagecalculator.MULTIPLE_DOT_PATTERN
import com.erman.percentagecalculator.MULTIPLE_DOT_REPLACEMENT
import com.erman.percentagecalculator.architecture.stores.Store
import com.erman.percentagecalculator.architecture.viewModels.ViewModel
import com.erman.percentagecalculator.domain.CalculationEvent
import com.erman.percentagecalculator.domain.CalculationState
import com.erman.percentagecalculator.domain.LoggingMiddleware
import com.erman.percentagecalculator.domain.Reducer

class PercentageCalculatorViewModel :
    ViewModel<CalculationState, CalculationEvent, PercentageCalculatorViewModel.ViewState>,
    androidx.lifecycle.ViewModel() {
    data class ViewState(val result: Double = 0.0, val error: String? = null)

    private val observer: Observer<CalculationState> = Observer { onStateChange(it) }
    override var state: MutableLiveData<ViewState> = MutableLiveData()
    override val store: Store<CalculationState, CalculationEvent> =
        Store(
            MutableLiveData(CalculationState()),
            reducer = Reducer(),
            middlewares = listOf(LoggingMiddleware())
        )

    init {
        store.state.observeForever(observer)
    }

    override fun onCleared() {
        super.onCleared()
        store.state.removeObserver(observer)
    }

    override fun onStateChange(newState: CalculationState) {
        state.value = ViewState(result = newState.result, error = newState.error)
    }

    fun sanitizeInput(input: String): String {
        return input.replace(CHAR_TO_BE_IGNORED, IGNORED_CHAR_REPLACEMENT)
            .replace(Regex(MULTIPLE_DOT_PATTERN), MULTIPLE_DOT_REPLACEMENT)
    }
}
