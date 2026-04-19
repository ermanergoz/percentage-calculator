package com.erman.percentagecalculator.architecture.stores

import android.util.Log
import com.erman.percentagecalculator.architecture.common.Dispatcher
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.architecture.reducers.Reducer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Store<State, Event>(
    initialState: State,
    private val reducer: Reducer<State, Event>,
    private val middlewares: List<Middleware<State, Event>> = emptyList(),
    private val scope: CoroutineScope,
) : Dispatcher<Event> {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val mutex = Mutex()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Log.e("Store", "Unhandled exception in dispatch", throwable)
        }

    override fun dispatch(event: Event) {
        scope.launch(exceptionHandler) {
            mutex.withLock {
                val currentState = _state.value

                var modifiedEvent = event
                for (middleware in middlewares) {
                    modifiedEvent = middleware.apply(currentState, modifiedEvent)
                }

                _state.value = reducer.reduce(currentState, modifiedEvent)
            }
        }
    }
}
