package com.erman.percentagecalculator.architecture.reducers

interface Reducer<State, Event> {
    fun reduce(
        state: State,
        event: Event,
    ): State
}
