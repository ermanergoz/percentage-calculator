package com.erman.percentagecalculator.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.architecture.reducers.Reducer
import com.erman.percentagecalculator.architecture.stores.Store
import com.erman.percentagecalculator.architecture.viewModels.ReduxViewModel
import com.erman.percentagecalculator.domain.tipcalculator.TipCalculatorEvent
import com.erman.percentagecalculator.domain.tipcalculator.TipCalculatorState
import kotlinx.coroutines.flow.StateFlow

class TipCalculatorViewModel(
    reducer: Reducer<TipCalculatorState, TipCalculatorEvent>,
    middlewares: List<Middleware<TipCalculatorState, TipCalculatorEvent>>,
) : ReduxViewModel<TipCalculatorState, TipCalculatorEvent>() {
    override val store =
        Store(
            initialState = TipCalculatorState(),
            reducer = reducer,
            middlewares = middlewares,
            scope = viewModelScope,
        )
    override val state: StateFlow<TipCalculatorState> = store.state
}
