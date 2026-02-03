package com.erman.percentagecalculator.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.architecture.reducers.Reducer
import com.erman.percentagecalculator.architecture.stores.Store
import com.erman.percentagecalculator.architecture.viewModels.ReduxViewModel
import com.erman.percentagecalculator.domain.home.HomeEvent
import com.erman.percentagecalculator.domain.home.HomeState
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
    reducer: Reducer<HomeState, HomeEvent>,
    middlewares: List<Middleware<HomeState, HomeEvent>>,
) : ReduxViewModel<HomeState, HomeEvent>() {
    override val store =
        Store(
            initialState = HomeState(),
            reducer = reducer,
            middlewares = middlewares,
            scope = viewModelScope,
        )

    override val state: StateFlow<HomeState> = store.state

    init {
        dispatch(HomeEvent.LoadOperations)
    }
}
