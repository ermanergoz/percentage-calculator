package com.erman.percentagecalculator.architecture.viewModels

import androidx.lifecycle.MutableLiveData
import com.erman.percentagecalculator.architecture.common.Dispatching
import com.erman.percentagecalculator.architecture.stores.Store

interface ViewModel<State, Event, ViewState> : Dispatching<Event> {
    var state: MutableLiveData<ViewState>
    val store: Store<State, Event>

    fun onStateChange(newState: State)

    override fun dispatch(event: Event) {
        store.dispatch(event = event)
    }
}
