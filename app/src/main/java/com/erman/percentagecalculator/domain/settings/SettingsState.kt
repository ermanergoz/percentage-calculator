package com.erman.percentagecalculator.domain.settings

import com.erman.percentagecalculator.domain.model.ThemeMode

data class SettingsState(
    val language: String = "",
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val sortByUsage: Boolean = false,
)
