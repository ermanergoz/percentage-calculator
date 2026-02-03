package com.erman.percentagecalculator.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.architecture.reducers.Reducer
import com.erman.percentagecalculator.architecture.stores.Store
import com.erman.percentagecalculator.architecture.viewModels.ReduxViewModel
import com.erman.percentagecalculator.domain.CalculationEvent
import com.erman.percentagecalculator.domain.CalculationState
import kotlinx.coroutines.flow.StateFlow

class PercentageCalculatorViewModel(
    reducer: Reducer<CalculationState, CalculationEvent>,
    middlewares: List<Middleware<CalculationState, CalculationEvent>>,
) : ReduxViewModel<CalculationState, CalculationEvent>() {
    override val store: Store<CalculationState, CalculationEvent> =
        Store(
            initialState = CalculationState(),
            reducer = reducer,
            middlewares = middlewares,
            scope = viewModelScope,
        )

    override val state: StateFlow<CalculationState> get() = store.state
}
