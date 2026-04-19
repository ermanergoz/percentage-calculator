package com.erman.percentagecalculator.domain.service

import com.erman.percentagecalculator.domain.model.Operation

interface HistoryService {
    suspend fun write(
        operation: Operation,
        inputs: List<String>,
        result: Double,
        secondaryResult: Double? = null,
    )
}
