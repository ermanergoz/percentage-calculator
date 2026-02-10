package com.erman.percentagecalculator.domain.model

data class BatchResultItem(
    val input: String,
    val result: Double,
    val secondaryResult: Double? = null,
)
