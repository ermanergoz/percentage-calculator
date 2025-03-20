package com.erman.percentagecalculator.architecture.common

interface Dispatching<Event> {
    fun dispatch(event: Event)
}
