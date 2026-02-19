package com.erman.percentagecalculator.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.architecture.reducers.Reducer
import com.erman.percentagecalculator.architecture.stores.Store
import com.erman.percentagecalculator.architecture.viewModels.ReduxViewModel
import com.erman.percentagecalculator.domain.history.HistoryEvent
import com.erman.percentagecalculator.domain.history.HistoryState
import kotlinx.coroutines.flow.StateFlow

class HistoryViewModel(
    reducer: Reducer<HistoryState, HistoryEvent>,
    middlewares: List<Middleware<HistoryState, HistoryEvent>>,
) : ReduxViewModel<HistoryState, HistoryEvent>() {
    override val store =
        Store(
            initialState = HistoryState(),
            reducer = reducer,
            middlewares = middlewares,
            scope = viewModelScope,
        )

    override val state: StateFlow<HistoryState> = store.state

    init {
        dispatch(HistoryEvent.LoadHistory)
    }
}
