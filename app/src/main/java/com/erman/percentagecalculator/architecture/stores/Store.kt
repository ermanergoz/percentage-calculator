package com.erman.percentagecalculator.architecture.stores

import androidx.lifecycle.MutableLiveData
import com.erman.percentagecalculator.architecture.common.Dispatching
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.architecture.reducers.Reducing
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Store<State, Event>(
    var state: MutableLiveData<State>,
    val reducer: Reducing<State, Event>,
    private val middlewares: List<Middleware<State, Event>> = emptyList()
) : Dispatching<Event> {

    @OptIn(DelicateCoroutinesApi::class)
    override fun dispatch(event: Event) {
        GlobalScope.launch {
            val modifiedEvent = middlewares.fold(event) { accEvent, middleware ->
                middleware.apply(state, accEvent)
            }
            val newEvent = reducer.reduce(state, modifiedEvent)

            newEvent?.let {
                dispatch(it)
            }
        }
    }
}
