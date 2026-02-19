package com.erman.percentagecalculator.domain.model

data class HistoryItem(
    val id: Long = 0,
    val operation: Operation,
    val inputs: List<String>,
    val result: Double,
    val secondaryResult: Double? = null,
    val timestamp: Long,
)
