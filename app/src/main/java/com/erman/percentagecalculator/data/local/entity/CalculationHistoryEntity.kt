package com.erman.percentagecalculator.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_history")
data class CalculationHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val operation: String,
    val inputs: String,
    val result: Double,
    val secondaryResult: Double? = null,
    val timestamp: Long,
)
