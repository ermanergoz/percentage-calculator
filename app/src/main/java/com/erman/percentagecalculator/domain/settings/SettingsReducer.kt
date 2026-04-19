package com.erman.percentagecalculator.domain.settings

import com.erman.percentagecalculator.architecture.reducers.Reducer

class SettingsReducer : Reducer<SettingsState, SettingsEvent> {
    override fun reduce(
        state: SettingsState,
        event: SettingsEvent,
    ): SettingsState {
        return when (event) {
            is SettingsEvent.LoadSettings -> state
            is SettingsEvent.UpdateTheme -> state.copy(theme = event.theme)
            is SettingsEvent.UpdateLanguage -> state.copy(language = event.languageCode)
            is SettingsEvent.UpdateSortByUsage -> state.copy(sortByUsage = event.enabled)
            is SettingsEvent.SettingsLoaded ->
                state.copy(
                    language = event.language,
                    theme = event.theme,
                    sortByUsage = event.sortByUsage,
                )
        }
    }
}
