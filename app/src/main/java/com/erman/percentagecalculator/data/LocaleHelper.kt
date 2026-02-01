package com.erman.percentagecalculator.data

import android.content.Context
import android.content.res.Configuration
import com.erman.percentagecalculator.LANGUAGE_PREF_KEY
import com.erman.percentagecalculator.PREFS_NAME
import java.util.Locale

object LocaleHelper {
    fun applyLocale(context: Context): Context {
        val languageCode =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(LANGUAGE_PREF_KEY, "") ?: ""
        if (languageCode.isEmpty()) return context
        return updateContextLocale(context, languageCode)
    }

    private fun updateContextLocale(
        context: Context,
        languageCode: String,
    ): Context {
        val locale = buildLocale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    private fun buildLocale(languageCode: String): Locale {
        return when {
            languageCode.contains("-r") -> {
                val parts = languageCode.split("-r")
                Locale.Builder().setLanguage(parts[0]).setRegion(parts[1]).build()
            }
            else -> Locale.Builder().setLanguage(languageCode).build()
        }
    }
}
