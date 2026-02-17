package com.erman.percentagecalculator.domain.history

import com.erman.percentagecalculator.domain.model.HistoryItem

data class HistoryState(
    val entries: List<HistoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
