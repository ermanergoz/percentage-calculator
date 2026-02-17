package com.erman.percentagecalculator.domain.settings

import com.erman.percentagecalculator.domain.model.ThemeMode

sealed class SettingsEvent {
    data object LoadSettings : SettingsEvent()

    data class UpdateTheme(val theme: ThemeMode) : SettingsEvent()

    data class UpdateLanguage(val languageCode: String) : SettingsEvent()

    data class UpdateSortByUsage(val enabled: Boolean) : SettingsEvent()

    data class SettingsLoaded(val language: String, val theme: ThemeMode, val sortByUsage: Boolean) : SettingsEvent()
}
