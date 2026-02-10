package com.erman.percentagecalculator.domain.batch

import com.erman.percentagecalculator.UNKNOWN_ERROR
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.domain.model.BatchResultItem
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.domain.service.CalculationService
import com.erman.percentagecalculator.domain.service.HistoryService

private const val ERROR_NO_OPERATION: String = "No operation selected"
private const val ERROR_INVALID_FIXED_VALUE: String = "Please enter a valid fixed value"

class BatchMiddleware(
    private val calculationService: CalculationService,
    private val historyService: HistoryService,
) : Middleware<BatchState, BatchEvent> {
    override suspend fun apply(
        state: BatchState,
        event: BatchEvent,
    ): BatchEvent {
        if (event !is BatchEvent.Calculate) return event
        return calculateBatch(state)
    }

    @Suppress("ReturnCount")
    private suspend fun calculateBatch(state: BatchState): BatchEvent {
        val operation =
            state.operation ?: return BatchEvent.BatchFailed(ERROR_NO_OPERATION)
        val fixedValue =
            state.fixedInput.toDoubleOrNull()
                ?: return BatchEvent.BatchFailed(ERROR_INVALID_FIXED_VALUE)

        return try {
            val results =
                state.batchInputs.map { input ->
                    computeResult(operation, fixedValue, input)
                }
            results.forEach { item ->
                historyService.write(
                    operation,
                    listOf(item.input, state.fixedInput),
                    item.result,
                    item.secondaryResult,
                )
            }
            BatchEvent.BatchComplete(results)
        } catch (
            @Suppress("TooGenericExceptionCaught") e: Throwable,
        ) {
            BatchEvent.BatchFailed(e.message ?: UNKNOWN_ERROR)
        }
    }

    private fun computeResult(
        operation: Operation,
        fixedValue: Double,
        batchInput: String,
    ): BatchResultItem {
        val batchValue =
            batchInput.toDoubleOrNull()
                ?: throw IllegalArgumentException("Invalid input: $batchInput")

        val item = computeSingle(operation, batchValue, fixedValue, batchInput)
        validateResult(item, batchInput)
        return item
    }

    @Suppress("CyclomaticComplexMethod", "LongMethod")
    private fun computeSingle(
        operation: Operation,
        batchValue: Double,
        fixedValue: Double,
        batchInput: String,
    ): BatchResultItem {
        return when (operation) {
            Operation.FIND_PERCENTAGE ->
                BatchResultItem(
                    input = batchInput,
                    result = calculationService.findPercentage(batchValue, fixedValue),
                )
            Operation.PERCENTAGE_OF_VALUE ->
                BatchResultItem(
                    input = batchInput,
                    result = calculationService.findPercentageOfValue(fixedValue, batchValue),
                )
            Operation.INCREASE_PERCENTAGE ->
                BatchResultItem(
                    input = batchInput,
                    result = calculationService.increaseByPercentage(batchValue, fixedValue),
                )
            Operation.DECREASE_PERCENTAGE ->
                BatchResultItem(
                    input = batchInput,
                    result = calculationService.decreaseByPercentage(batchValue, fixedValue),
                )
            Operation.PERCENTAGE_CHANGE ->
                BatchResultItem(
                    input = batchInput,
                    result = calculationService.percentageChange(fixedValue, batchValue),
                )
            Operation.FRACTION_TO_PERCENTAGE ->
                BatchResultItem(
                    input = batchInput,
                    result = calculationService.fractionToPercentage(batchValue, fixedValue),
                )
            Operation.DISCOUNT -> {
                val (primary, secondary) =
                    calculationService.calculateDiscount(batchValue, fixedValue)
                BatchResultItem(
                    input = batchInput,
                    result = primary,
                    secondaryResult = secondary,
                )
            }
            Operation.MARKUP -> {
                val (primary, secondary) =
                    calculationService.calculateMarkup(batchValue, fixedValue)
                BatchResultItem(
                    input = batchInput,
                    result = primary,
                    secondaryResult = secondary,
                )
            }
            Operation.TAX -> {
                val (primary, secondary) =
                    calculationService.calculateTax(batchValue, fixedValue)
                BatchResultItem(
                    input = batchInput,
                    result = primary,
                    secondaryResult = secondary,
                )
            }
            Operation.GPA_CONVERTER,
            Operation.TIP_CALCULATOR,
            Operation.COMPOUND_INTEREST,
            -> error("Operation $operation does not support batch mode")
        }
    }

    private fun validateResult(
        item: BatchResultItem,
        batchInput: String,
    ) {
        val hasInvalidResult =
            item.result.isInfinite() || item.result.isNaN() ||
                (
                    item.secondaryResult != null &&
                        (item.secondaryResult.isInfinite() || item.secondaryResult.isNaN())
                )
        if (hasInvalidResult) {
            throw ArithmeticException("Result is undefined for input: $batchInput")
        }
    }
}
