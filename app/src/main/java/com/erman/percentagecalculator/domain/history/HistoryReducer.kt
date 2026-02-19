package com.erman.percentagecalculator.domain.history

import com.erman.percentagecalculator.architecture.reducers.Reducer

class HistoryReducer : Reducer<HistoryState, HistoryEvent> {
    override fun reduce(
        state: HistoryState,
        event: HistoryEvent,
    ): HistoryState {
        return when (event) {
            is HistoryEvent.LoadHistory -> state.copy(isLoading = true, error = null)
            is HistoryEvent.HistoryLoaded -> state.copy(entries = event.entries, isLoading = false)
            is HistoryEvent.HistoryError -> state.copy(error = event.message, isLoading = false)
            is HistoryEvent.DeleteEntry,
            is HistoryEvent.ClearAll,
            -> state.copy(isLoading = true)
        }
    }
}
