package com.erman.percentagecalculator.domain

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.erman.percentagecalculator.architecture.middlewares.Middleware

class LoggingMiddleware :
    Middleware<AppState, CalculationEvent> {
    override suspend fun apply(state: MutableLiveData<AppState>, event: CalculationEvent): CalculationEvent {
        Log.i("Middleware", "Dispatching event: $event")
        return event
    }
}
