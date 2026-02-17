package com.erman.percentagecalculator.domain.settings

import com.erman.percentagecalculator.architecture.middlewares.Middleware
import com.erman.percentagecalculator.domain.repository.PreferencesRepository

class SettingsMiddleware(
    private val preferencesRepository: PreferencesRepository,
) : Middleware<SettingsState, SettingsEvent> {
    override suspend fun apply(
        state: SettingsState,
        event: SettingsEvent,
    ): SettingsEvent {
        return when (event) {
            is SettingsEvent.LoadSettings ->
                SettingsEvent.SettingsLoaded(
                    language = preferencesRepository.getLanguage(),
                    theme = preferencesRepository.getTheme(),
                    sortByUsage = preferencesRepository.isSortByUsageEnabled(),
                )
            is SettingsEvent.UpdateTheme -> {
                preferencesRepository.setTheme(event.theme)
                event
            }
            is SettingsEvent.UpdateLanguage -> {
                preferencesRepository.setLanguage(event.languageCode)
                event
            }
            is SettingsEvent.UpdateSortByUsage -> {
                preferencesRepository.setSortByUsageEnabled(event.enabled)
                event
            }
            is SettingsEvent.SettingsLoaded -> event
        }
    }
}
