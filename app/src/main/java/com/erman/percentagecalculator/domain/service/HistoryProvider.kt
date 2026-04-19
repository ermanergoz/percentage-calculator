package com.erman.percentagecalculator.domain.service

import com.erman.percentagecalculator.domain.model.HistoryItem
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.domain.repository.HistoryRepository

class HistoryProvider(
    private val historyRepository: HistoryRepository,
) : HistoryService {
    override suspend fun write(
        operation: Operation,
        inputs: List<String>,
        result: Double,
        secondaryResult: Double?,
    ) {
        try {
            historyRepository.insertEntry(
                HistoryItem(
                    operation = operation,
                    inputs = inputs,
                    result = result,
                    secondaryResult = secondaryResult,
                    timestamp = System.currentTimeMillis(),
                ),
            )
        } catch (
            @Suppress("TooGenericExceptionCaught") e: Throwable,
        ) {
            android.util.Log.e("HistoryProvider", "Failed to write history", e)
        }
    }
}
