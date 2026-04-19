package com.erman.percentagecalculator.domain.home

import com.erman.percentagecalculator.domain.model.Operation

sealed class HomeEvent {
    data object LoadOperations : HomeEvent()

    data class OperationsLoaded(
        val operations: List<Operation>,
        val sortByUsage: Boolean,
    ) : HomeEvent()
}
