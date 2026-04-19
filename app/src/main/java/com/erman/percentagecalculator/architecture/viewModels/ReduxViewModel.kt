package com.erman.percentagecalculator.architecture.viewModels

import androidx.lifecycle.ViewModel
import com.erman.percentagecalculator.architecture.common.Dispatcher
import com.erman.percentagecalculator.architecture.stores.Store
import kotlinx.coroutines.flow.StateFlow

abstract class ReduxViewModel<State, Event> : ViewModel(), Dispatcher<Event> {
    protected abstract val store: Store<State, Event>
    abstract val state: StateFlow<State>

    override fun dispatch(event: Event) {
        store.dispatch(event)
    }
}
