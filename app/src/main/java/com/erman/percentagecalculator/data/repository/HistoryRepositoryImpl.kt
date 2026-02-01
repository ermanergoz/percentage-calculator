package com.erman.percentagecalculator.data.repository

import com.erman.percentagecalculator.INPUT_SEPARATOR
import com.erman.percentagecalculator.data.local.dao.HistoryDao
import com.erman.percentagecalculator.data.local.entity.CalculationHistoryEntity
import com.erman.percentagecalculator.domain.model.HistoryItem
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao,
) : HistoryRepository {
    override suspend fun insertEntry(item: HistoryItem) {
        historyDao.insert(item.toEntity())
    }

    override suspend fun getAllEntries(): List<HistoryItem> {
        return historyDao.getAll().mapNotNull { it.toDomainModel() }
    }

    override suspend fun deleteEntry(id: Long) {
        historyDao.deleteById(id)
    }

    override suspend fun clearAll() {
        historyDao.deleteAll()
    }

    override suspend fun getOperationUsageCounts(): Map<Operation, Int> {
        return historyDao.getOperationUsageCounts().mapNotNull { usage ->
            try {
                Operation.valueOf(usage.operation) to usage.count
            } catch (_: IllegalArgumentException) {
                null
            }
        }.toMap()
    }

    private fun HistoryItem.toEntity(): CalculationHistoryEntity {
        return CalculationHistoryEntity(
            id = id,
            operation = operation.name,
            inputs = inputs.joinToString(separator = INPUT_SEPARATOR),
            result = result,
            secondaryResult = secondaryResult,
            timestamp = timestamp,
        )
    }

    private fun CalculationHistoryEntity.toDomainModel(): HistoryItem? {
        val op =
            try {
                Operation.valueOf(operation)
            } catch (_: IllegalArgumentException) {
                return null
            }
        return HistoryItem(
            id = id,
            operation = op,
            inputs = if (inputs.isEmpty()) emptyList() else inputs.split(INPUT_SEPARATOR),
            result = result,
            secondaryResult = secondaryResult,
            timestamp = timestamp,
        )
    }
}
