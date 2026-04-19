package com.erman.percentagecalculator.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.erman.percentagecalculator.domain.model.ThemeMode

private val DarkColorPalette =
    darkColors(
        primary = DarkBlue,
        primaryVariant = DarkBlue,
        secondary = LightBlue,
        background = DarkerBlue,
        surface = DarkSurface,
        error = ErrorRedDark,
        onPrimary = Color.White,
    )

private val LightColorPalette =
    lightColors(
        primary = DarkBlue,
        primaryVariant = DarkBlue,
        secondary = LightBlue,
        background = LighterBlue,
        surface = Color.White,
        error = ErrorRed,
    )

@Composable
fun PercentageCalculatorTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme =
        when (themeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }

    val colors =
        if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
