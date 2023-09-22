package com.erman.percentagecalculator.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.erman.percentagecalculator.architecture.stores.Store
import com.erman.percentagecalculator.architecture.viewModels.ViewModel
import com.erman.percentagecalculator.domain.AppState
import com.erman.percentagecalculator.domain.CalculationEvent
import com.erman.percentagecalculator.domain.LoggingMiddleware
import com.erman.percentagecalculator.domain.Reducer

class PercentageCalculatorViewModel :
    ViewModel<AppState, CalculationEvent, PercentageCalculatorViewModel.ViewState>,
    androidx.lifecycle.ViewModel() {
    data class ViewState(val result: Double = 0.0, val error: String? = null)

    override var state: MutableLiveData<ViewState> = MutableLiveData()
    override val store: Store<AppState, CalculationEvent> =
        Store(
            MutableLiveData(AppState()),
            reducer = Reducer(),
            middlewares = listOf(LoggingMiddleware())
        )

    private val observer: Observer<AppState> = Observer { onStateChange(it) }

    init {
        store.state.observeForever(observer)
    }

    override fun onCleared() {
        super.onCleared()
        store.state.removeObserver(observer)
    }

    override fun onStateChange(newState: AppState) {
        state.value = ViewState(result = newState.result, error = newState.error)
    }
}
