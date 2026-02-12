package com.erman.percentagecalculator.domain.tipcalculator

import com.erman.percentagecalculator.ERROR_INVALID_NUMBERS
import com.erman.percentagecalculator.ERROR_UNDEFINED_RESULT
import com.erman.percentagecalculator.UNKNOWN_ERROR
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.domain.service.HistoryService
import com.erman.percentagecalculator.domain.service.TipCalculationService

private const val DEFAULT_SPLIT_COUNT: Int = 1

class TipCalculatorMiddleware(
    private val tipCalculationService: TipCalculationService,
    private val historyService: HistoryService,
) : Middleware<TipCalculatorState, TipCalculatorEvent> {
    @Suppress("ReturnCount")
    override suspend fun apply(
        state: TipCalculatorState,
        event: TipCalculatorEvent,
    ): TipCalculatorEvent {
        if (event !is TipCalculatorEvent.Calculate) return event
        return try {
            val bill = state.billAmount.toDouble()
            val tipPct = state.tipPercentage.toDouble()
            val split = state.splitCount.toIntOrNull() ?: DEFAULT_SPLIT_COUNT
            val result = tipCalculationService.calculate(bill, tipPct, split)
            val hasInvalidResult =
                result.tipAmount.isInfinite() || result.tipAmount.isNaN() ||
                    result.totalAmount.isInfinite() || result.totalAmount.isNaN() ||
                    result.perPersonAmount.isInfinite() || result.perPersonAmount.isNaN()
            if (hasInvalidResult) {
                return TipCalculatorEvent.CalculationFailed(ERROR_UNDEFINED_RESULT)
            }
            historyService.write(
                operation = Operation.TIP_CALCULATOR,
                inputs = listOf(state.billAmount, state.tipPercentage, state.splitCount),
                result = result.totalAmount,
                secondaryResult = result.tipAmount,
            )
            TipCalculatorEvent.CalculationComplete(
                tipAmount = result.tipAmount,
                totalAmount = result.totalAmount,
                perPersonAmount = result.perPersonAmount,
            )
        } catch (_: NumberFormatException) {
            TipCalculatorEvent.CalculationFailed(ERROR_INVALID_NUMBERS)
        } catch (
            @Suppress("TooGenericExceptionCaught") e: Throwable,
        ) {
            TipCalculatorEvent.CalculationFailed(e.message ?: UNKNOWN_ERROR)
        }
    }
}
