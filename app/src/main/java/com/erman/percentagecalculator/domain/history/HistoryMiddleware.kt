package com.erman.percentagecalculator.domain.history

import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.domain.repository.HistoryRepository

class HistoryMiddleware(
    private val historyRepository: HistoryRepository,
) : Middleware<HistoryState, HistoryEvent> {
    override suspend fun apply(
        state: HistoryState,
        event: HistoryEvent,
    ): HistoryEvent {
        return when (event) {
            is HistoryEvent.LoadHistory -> {
                try {
                    HistoryEvent.HistoryLoaded(historyRepository.getAllEntries())
                } catch (
                    @Suppress("TooGenericExceptionCaught") e: Throwable,
                ) {
                    HistoryEvent.HistoryError(e.message ?: "Failed to load history")
                }
            }
            is HistoryEvent.DeleteEntry -> {
                try {
                    historyRepository.deleteEntry(event.id)
                    HistoryEvent.HistoryLoaded(historyRepository.getAllEntries())
                } catch (
                    @Suppress("TooGenericExceptionCaught") e: Throwable,
                ) {
                    HistoryEvent.HistoryError(e.message ?: "Failed to delete entry")
                }
            }
            is HistoryEvent.ClearAll -> {
                try {
                    historyRepository.clearAll()
                    HistoryEvent.HistoryLoaded(emptyList())
                } catch (
                    @Suppress("TooGenericExceptionCaught") e: Throwable,
                ) {
                    HistoryEvent.HistoryError(e.message ?: "Failed to clear history")
                }
            }
            is HistoryEvent.HistoryLoaded,
            is HistoryEvent.HistoryError,
            -> event
        }
    }
}
