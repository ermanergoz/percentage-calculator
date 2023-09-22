package com.erman.percentagecalculator.architecture.middlewares

import androidx.lifecycle.MutableLiveData

interface Middleware<State, Event> {
    suspend fun apply(state: MutableLiveData<State>, event: Event): Event
}
