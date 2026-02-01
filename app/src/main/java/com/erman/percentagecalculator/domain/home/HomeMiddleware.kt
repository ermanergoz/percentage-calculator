package com.erman.percentagecalculator.domain.home

import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.domain.repository.HistoryRepository
import com.erman.percentagecalculator.domain.repository.PreferencesRepository

class HomeMiddleware(
    private val historyRepository: HistoryRepository,
    private val preferencesRepository: PreferencesRepository,
) : Middleware<HomeState, HomeEvent> {
    override suspend fun apply(
        state: HomeState,
        event: HomeEvent,
    ): HomeEvent {
        if (event !is HomeEvent.LoadOperations) return event
        val sortByUsage = preferencesRepository.isSortByUsageEnabled()
        return HomeEvent.OperationsLoaded(
            operations =
                if (sortByUsage) {
                    val usageCounts = historyRepository.getOperationUsageCounts()
                    Operation.entries.sortedByDescending { usageCounts[it] ?: 0 }
                } else {
                    Operation.entries
                },
            sortByUsage = sortByUsage,
        )
    }
}
