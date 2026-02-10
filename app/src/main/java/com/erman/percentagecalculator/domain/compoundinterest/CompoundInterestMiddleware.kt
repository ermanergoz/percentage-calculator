package com.erman.percentagecalculator.domain.compoundinterest

import com.erman.percentagecalculator.ERROR_INVALID_NUMBERS
import com.erman.percentagecalculator.ERROR_UNDEFINED_RESULT
import com.erman.percentagecalculator.UNKNOWN_ERROR
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.domain.service.CompoundInterestService
import com.erman.percentagecalculator.domain.service.HistoryService

private const val DEFAULT_COMPOUNDS_PER_YEAR: Int = 12

class CompoundInterestMiddleware(
    private val compoundInterestService: CompoundInterestService,
    private val historyService: HistoryService,
) : Middleware<CompoundInterestState, CompoundInterestEvent> {
    @Suppress("ReturnCount")
    override suspend fun apply(
        state: CompoundInterestState,
        event: CompoundInterestEvent,
    ): CompoundInterestEvent {
        if (event !is CompoundInterestEvent.Calculate) return event
        return try {
            val principal = state.principal.toDouble()
            val rate = state.annualRate.toDouble()
            val time = state.timeInYears.toDouble()
            val compounds =
                state.compoundsPerYear.toIntOrNull() ?: DEFAULT_COMPOUNDS_PER_YEAR
            val result =
                compoundInterestService.calculate(principal, rate, time, compounds)
            val hasInvalidResult =
                result.futureValue.isInfinite() || result.futureValue.isNaN() ||
                    result.totalInterest.isInfinite() || result.totalInterest.isNaN()
            if (hasInvalidResult) {
                return CompoundInterestEvent.CalculationFailed(ERROR_UNDEFINED_RESULT)
            }
            historyService.write(
                operation = Operation.COMPOUND_INTEREST,
                inputs = listOf(state.principal, state.annualRate, state.timeInYears, state.compoundsPerYear),
                result = result.futureValue,
                secondaryResult = result.totalInterest,
            )
            CompoundInterestEvent.CalculationComplete(
                futureValue = result.futureValue,
                totalInterest = result.totalInterest,
            )
        } catch (_: NumberFormatException) {
            CompoundInterestEvent.CalculationFailed(ERROR_INVALID_NUMBERS)
        } catch (
            @Suppress("TooGenericExceptionCaught") e: Throwable,
        ) {
            CompoundInterestEvent.CalculationFailed(e.message ?: UNKNOWN_ERROR)
        }
    }
}
