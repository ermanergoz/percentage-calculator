package com.erman.percentagecalculator.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.erman.percentagecalculator.LANGUAGE_PREF_KEY
import com.erman.percentagecalculator.PREFS_NAME
import com.erman.percentagecalculator.domain.model.ThemeMode
import com.erman.percentagecalculator.domain.repository.PreferencesRepository

private const val THEME_PREF_KEY: String = "selected_theme"
private const val SORT_BY_USAGE_PREF_KEY: String = "sort_by_usage"

class PreferencesRepositoryImpl(
    context: Context,
) : PreferencesRepository {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getLanguage(): String {
        return prefs.getString(LANGUAGE_PREF_KEY, "") ?: ""
    }

    override fun setLanguage(languageCode: String) {
        prefs.edit().putString(LANGUAGE_PREF_KEY, languageCode).apply()
    }

    override fun getTheme(): ThemeMode {
        return try {
            ThemeMode.valueOf(prefs.getString(THEME_PREF_KEY, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
        } catch (_: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }

    override fun setTheme(theme: ThemeMode) {
        prefs.edit { putString(THEME_PREF_KEY, theme.name) }
    }

    override fun isSortByUsageEnabled(): Boolean {
        return prefs.getBoolean(SORT_BY_USAGE_PREF_KEY, false)
    }

    override fun setSortByUsageEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(SORT_BY_USAGE_PREF_KEY, enabled) }
    }
}
