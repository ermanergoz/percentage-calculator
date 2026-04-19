package com.erman.percentagecalculator.domain.repository

import com.erman.percentagecalculator.domain.model.ThemeMode

interface PreferencesRepository {
    fun getLanguage(): String

    fun setLanguage(languageCode: String)

    fun getTheme(): ThemeMode

    fun setTheme(theme: ThemeMode)

    fun isSortByUsageEnabled(): Boolean

    fun setSortByUsageEnabled(enabled: Boolean)
}
