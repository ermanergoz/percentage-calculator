package com.erman.percentagecalculator.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.architecture.reducers.Reducer
import com.erman.percentagecalculator.architecture.stores.Store
import com.erman.percentagecalculator.architecture.viewModels.ReduxViewModel
import com.erman.percentagecalculator.domain.batch.BatchEvent
import com.erman.percentagecalculator.domain.batch.BatchState
import kotlinx.coroutines.flow.StateFlow

class BatchViewModel(
    reducer: Reducer<BatchState, BatchEvent>,
    middlewares: List<Middleware<BatchState, BatchEvent>>,
) : ReduxViewModel<BatchState, BatchEvent>() {
    override val store =
        Store(
            initialState = BatchState(),
            reducer = reducer,
            middlewares = middlewares,
            scope = viewModelScope,
        )
    override val state: StateFlow<BatchState> = store.state
}
