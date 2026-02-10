package com.erman.percentagecalculator.domain.batch

import com.erman.percentagecalculator.domain.model.BatchResultItem
import com.erman.percentagecalculator.domain.model.Operation

sealed class BatchEvent {
    data class SelectOperation(val operation: Operation) : BatchEvent()

    data class UpdateFixedInput(val value: String) : BatchEvent()

    data class AddBatchInput(val value: String = "") : BatchEvent()

    data class RemoveBatchInput(val index: Int) : BatchEvent()

    data class UpdateBatchInput(val index: Int, val value: String) : BatchEvent()

    data object Calculate : BatchEvent()

    data object Clear : BatchEvent()

    data class BatchComplete(val results: List<BatchResultItem>) : BatchEvent()

    data class BatchFailed(val message: String) : BatchEvent()
}
