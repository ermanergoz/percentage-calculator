package com.erman.percentagecalculator.domain.history

import com.erman.percentagecalculator.domain.model.HistoryItem

sealed class HistoryEvent {
    data object LoadHistory : HistoryEvent()

    data class DeleteEntry(val id: Long) : HistoryEvent()

    data object ClearAll : HistoryEvent()

    data class HistoryLoaded(val entries: List<HistoryItem>) : HistoryEvent()

    data class HistoryError(val message: String) : HistoryEvent()
}
