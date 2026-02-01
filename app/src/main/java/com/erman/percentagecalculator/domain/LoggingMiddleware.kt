package com.erman.percentagecalculator.domain

import android.util.Log
import com.erman.percentagecalculator.BuildConfig
import com.erman.percentagecalculator.architecture.middlewares.Middleware

class LoggingMiddleware : Middleware<CalculationState, CalculationEvent> {
    override suspend fun apply(
        state: CalculationState,
        event: CalculationEvent,
    ): CalculationEvent {
        if (BuildConfig.DEBUG) {
            Log.i("Middleware", "Dispatching event: $event")
        }
        return event
    }
}
