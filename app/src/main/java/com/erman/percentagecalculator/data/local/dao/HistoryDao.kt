package com.erman.percentagecalculator.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.erman.percentagecalculator.data.local.entity.CalculationHistoryEntity

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(entity: CalculationHistoryEntity)

    @Query("SELECT * FROM calculation_history ORDER BY timestamp DESC")
    suspend fun getAll(): List<CalculationHistoryEntity>

    @Query("DELETE FROM calculation_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM calculation_history")
    suspend fun deleteAll()

    @Query("SELECT operation, COUNT(*) as count FROM calculation_history GROUP BY operation")
    suspend fun getOperationUsageCounts(): List<OperationUsageCount>
}

data class OperationUsageCount(
    val operation: String,
    val count: Int,
)
