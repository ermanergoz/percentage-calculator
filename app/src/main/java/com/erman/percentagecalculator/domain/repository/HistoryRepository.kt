package com.erman.percentagecalculator.domain.repository

import com.erman.percentagecalculator.domain.model.HistoryItem
import com.erman.percentagecalculator.domain.model.Operation

interface HistoryRepository {
    suspend fun insertEntry(item: HistoryItem)

    suspend fun getAllEntries(): List<HistoryItem>

    suspend fun deleteEntry(id: Long)

    suspend fun clearAll()

    suspend fun getOperationUsageCounts(): Map<Operation, Int>
}
