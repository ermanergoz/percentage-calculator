package com.erman.percentagecalculator.domain

import com.erman.percentagecalculator.ERROR_UNDEFINED_RESULT
import com.erman.percentagecalculator.UNKNOWN_ERROR
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.domain.service.CalculationService
import com.erman.percentagecalculator.domain.service.HistoryService

class CalculationMiddleware(
    private val calculationService: CalculationService,
    private val historyService: HistoryService,
) : Middleware<CalculationState, CalculationEvent> {
    override suspend fun apply(
        state: CalculationState,
        event: CalculationEvent,
    ): CalculationEvent {
        val result =
            when (event) {
                is CalculationEvent.CalculatePercentage ->
                    compute {
                        calculationService.findPercentage(event.part, event.whole)
                    }
                is CalculationEvent.PercentageOfValue ->
                    compute {
                        calculationService.findPercentageOfValue(event.percentage, event.value)
                    }
                is CalculationEvent.IncreasePercentage ->
                    compute {
                        calculationService.increaseByPercentage(event.value, event.percentage)
                    }
                is CalculationEvent.DecreasePercentage ->
                    compute {
                        calculationService.decreaseByPercentage(event.value, event.percentage)
                    }
                is CalculationEvent.PercentageChange ->
                    compute {
                        calculationService.percentageChange(event.oldValue, event.newValue)
                    }
                is CalculationEvent.FractionToPercentage ->
                    compute {
                        calculationService.fractionToPercentage(event.numerator, event.denominator)
                    }
                is CalculationEvent.CalculateDiscount ->
                    computeDual {
                        calculationService.calculateDiscount(event.originalPrice, event.discountPercent)
                    }
                is CalculationEvent.CalculateMarkup ->
                    computeDual {
                        calculationService.calculateMarkup(event.cost, event.markupPercent)
                    }
                is CalculationEvent.CalculateTax ->
                    computeDual {
                        calculationService.calculateTax(event.amount, event.taxRate)
                    }
                is CalculationEvent.CalculateGpa ->
                    compute {
                        calculationService.calculateGpa(event.percentage)
                    }
                is CalculationEvent.UpdateFirstInput,
                is CalculationEvent.UpdateSecondInput,
                is CalculationEvent.CalculationComplete,
                is CalculationEvent.CalculationFailed,
                -> event
            }

        if (result is CalculationEvent.CalculationComplete) {
            writeHistory(event, result)
        }

        return result
    }

    private suspend fun writeHistory(
        event: CalculationEvent,
        result: CalculationEvent.CalculationComplete,
    ) {
        val (operation, inputs) =
            when (event) {
                is CalculationEvent.CalculatePercentage ->
                    Operation.FIND_PERCENTAGE to
                        listOf(event.part.toString(), event.whole.toString())
                is CalculationEvent.PercentageOfValue ->
                    Operation.PERCENTAGE_OF_VALUE to
                        listOf(event.percentage.toString(), event.value.toString())
                is CalculationEvent.IncreasePercentage ->
                    Operation.INCREASE_PERCENTAGE to
                        listOf(event.value.toString(), event.percentage.toString())
                is CalculationEvent.DecreasePercentage ->
                    Operation.DECREASE_PERCENTAGE to
                        listOf(event.value.toString(), event.percentage.toString())
                is CalculationEvent.PercentageChange ->
                    Operation.PERCENTAGE_CHANGE to
                        listOf(event.oldValue.toString(), event.newValue.toString())
                is CalculationEvent.FractionToPercentage ->
                    Operation.FRACTION_TO_PERCENTAGE to
                        listOf(event.numerator.toString(), event.denominator.toString())
                is CalculationEvent.CalculateDiscount ->
                    Operation.DISCOUNT to
                        listOf(event.originalPrice.toString(), event.discountPercent.toString())
                is CalculationEvent.CalculateMarkup ->
                    Operation.MARKUP to
                        listOf(event.cost.toString(), event.markupPercent.toString())
                is CalculationEvent.CalculateTax ->
                    Operation.TAX to
                        listOf(event.amount.toString(), event.taxRate.toString())
                is CalculationEvent.CalculateGpa ->
                    Operation.GPA_CONVERTER to listOf(event.percentage.toString())
                else -> return
            }

        historyService.write(operation, inputs, result.result, result.secondaryResult)
    }

    private inline fun compute(calculation: () -> Double): CalculationEvent {
        return try {
            val result = calculation()
            if (result.isInfinite() || result.isNaN()) {
                CalculationEvent.CalculationFailed(ERROR_UNDEFINED_RESULT)
            } else {
                CalculationEvent.CalculationComplete(result)
            }
        } catch (
            @Suppress("TooGenericExceptionCaught") e: Throwable,
        ) {
            CalculationEvent.CalculationFailed(e.message ?: UNKNOWN_ERROR)
        }
    }

    private inline fun computeDual(calculation: () -> Pair<Double, Double>): CalculationEvent {
        return try {
            val (primary, secondary) = calculation()
            val hasInvalidResult =
                primary.isInfinite() || primary.isNaN() ||
                    secondary.isInfinite() || secondary.isNaN()
            if (hasInvalidResult) {
                CalculationEvent.CalculationFailed(ERROR_UNDEFINED_RESULT)
            } else {
                CalculationEvent.CalculationComplete(primary, secondary)
            }
        } catch (
            @Suppress("TooGenericExceptionCaught") e: Throwable,
        ) {
            CalculationEvent.CalculationFailed(e.message ?: UNKNOWN_ERROR)
        }
    }
}
