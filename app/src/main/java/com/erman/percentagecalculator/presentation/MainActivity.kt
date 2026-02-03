package com.erman.percentagecalculator.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.erman.percentagecalculator.EXTRA_WIDGET_OPERATION
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.domain.model.ThemeMode
import com.erman.percentagecalculator.domain.repository.PreferencesRepository
import com.erman.percentagecalculator.presentation.navigation.Navigation
import com.erman.percentagecalculator.presentation.theme.PercentageCalculatorTheme
import com.erman.percentagecalculator.data.LocaleHelper
import com.google.android.gms.ads.MobileAds
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private var themeMode by mutableStateOf(ThemeMode.SYSTEM)
    private val preferencesRepository: PreferencesRepository by inject()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        MobileAds.initialize(this) {}
        themeMode = preferencesRepository.getTheme()

        val initialOperation =
            intent.getStringExtra(EXTRA_WIDGET_OPERATION)?.let { name ->
                runCatching { Operation.valueOf(name) }.getOrNull()
            }

        setContent {
            PercentageCalculatorTheme(themeMode = themeMode) {
                Navigation(initialOperation = initialOperation)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        themeMode = preferencesRepository.getTheme()
    }
}
