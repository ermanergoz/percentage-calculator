package com.erman.percentagecalculator.architecture.middlewares

interface Middleware<State, Event> {
    suspend fun apply(
        state: State,
        event: Event,
    ): Event
}
