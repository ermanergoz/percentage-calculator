package com.erman.percentagecalculator.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.architecture.reducers.Reducer
import com.erman.percentagecalculator.architecture.stores.Store
import com.erman.percentagecalculator.architecture.viewModels.ReduxViewModel
import com.erman.percentagecalculator.domain.settings.SettingsEvent
import com.erman.percentagecalculator.domain.settings.SettingsState
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    reducer: Reducer<SettingsState, SettingsEvent>,
    middlewares: List<Middleware<SettingsState, SettingsEvent>>,
) : ReduxViewModel<SettingsState, SettingsEvent>() {
    override val store =
        Store(
            initialState = SettingsState(),
            reducer = reducer,
            middlewares = middlewares,
            scope = viewModelScope,
        )

    override val state: StateFlow<SettingsState> = store.state

    init {
        dispatch(SettingsEvent.LoadSettings)
    }
}
