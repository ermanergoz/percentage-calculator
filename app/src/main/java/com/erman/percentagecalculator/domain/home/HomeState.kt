package com.erman.percentagecalculator.domain.home

import com.erman.percentagecalculator.domain.model.Operation

data class HomeState(
    val operations: List<Operation> = Operation.entries,
    val sortByUsage: Boolean = false,
)
