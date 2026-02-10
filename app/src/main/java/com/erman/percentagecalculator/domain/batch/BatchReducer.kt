package com.erman.percentagecalculator.domain.batch

import com.erman.percentagecalculator.architecture.reducers.Reducer

class BatchReducer : Reducer<BatchState, BatchEvent> {
    override fun reduce(
        state: BatchState,
        event: BatchEvent,
    ): BatchState {
        return when (event) {
            is BatchEvent.SelectOperation -> state.copy(operation = event.operation)
            is BatchEvent.UpdateFixedInput -> state.copy(fixedInput = event.value)
            is BatchEvent.AddBatchInput ->
                state.copy(
                    batchInputs = state.batchInputs + event.value,
                )
            is BatchEvent.RemoveBatchInput ->
                state.copy(
                    batchInputs =
                        if (state.batchInputs.size > 1) {
                            state.batchInputs.filterIndexed { index, _ -> index != event.index }
                        } else {
                            state.batchInputs
                        },
                )
            is BatchEvent.UpdateBatchInput ->
                state.copy(
                    batchInputs =
                        state.batchInputs.mapIndexed { index, value ->
                            if (index == event.index) event.value else value
                        },
                )
            is BatchEvent.Calculate -> state
            is BatchEvent.Clear ->
                state.copy(
                    fixedInput = "",
                    batchInputs = listOf(""),
                    results = emptyList(),
                    error = null,
                )
            is BatchEvent.BatchComplete ->
                state.copy(
                    results = event.results,
                    error = null,
                )
            is BatchEvent.BatchFailed ->
                state.copy(
                    results = emptyList(),
                    error = event.message,
                )
        }
    }
}
