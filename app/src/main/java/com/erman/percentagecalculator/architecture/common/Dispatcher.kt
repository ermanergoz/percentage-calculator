package com.erman.percentagecalculator.architecture.common

interface Dispatcher<Event> {
    fun dispatch(event: Event)
}
