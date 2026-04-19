package com.erman.percentagecalculator.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.architecture.reducers.Reducer
import com.erman.percentagecalculator.architecture.stores.Store
import com.erman.percentagecalculator.architecture.viewModels.ReduxViewModel
import com.erman.percentagecalculator.domain.compoundinterest.CompoundInterestEvent
import com.erman.percentagecalculator.domain.compoundinterest.CompoundInterestState
import kotlinx.coroutines.flow.StateFlow

class CompoundInterestViewModel(
    reducer: Reducer<CompoundInterestState, CompoundInterestEvent>,
    middlewares: List<Middleware<CompoundInterestState, CompoundInterestEvent>>,
) : ReduxViewModel<CompoundInterestState, CompoundInterestEvent>() {
    override val store =
        Store(
            initialState = CompoundInterestState(),
            reducer = reducer,
            middlewares = middlewares,
            scope = viewModelScope,
        )
    override val state: StateFlow<CompoundInterestState> = store.state
}
