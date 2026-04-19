package com.erman.percentagecalculator.domain.home

import com.erman.percentagecalculator.architecture.reducers.Reducer

class HomeReducer : Reducer<HomeState, HomeEvent> {
    override fun reduce(
        state: HomeState,
        event: HomeEvent,
    ): HomeState {
        return when (event) {
            is HomeEvent.LoadOperations -> state
            is HomeEvent.OperationsLoaded ->
                state.copy(
                    operations = event.operations,
                    sortByUsage = event.sortByUsage,
                )
        }
    }
}
