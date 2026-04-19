package com.erman.percentagecalculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.erman.percentagecalculator.data.local.dao.HistoryDao
import com.erman.percentagecalculator.data.local.entity.CalculationHistoryEntity

@Database(entities = [CalculationHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
