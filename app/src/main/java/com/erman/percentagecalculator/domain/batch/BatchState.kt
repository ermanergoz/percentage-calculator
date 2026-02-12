package com.erman.percentagecalculator.domain.batch

import com.erman.percentagecalculator.domain.model.BatchResultItem
import com.erman.percentagecalculator.domain.model.Operation

data class BatchState(
    val operation: Operation? = null,
    val fixedInput: String = "",
    val batchInputs: List<String> = listOf(""),
    val results: List<BatchResultItem> = emptyList(),
    val error: String? = null,
)
