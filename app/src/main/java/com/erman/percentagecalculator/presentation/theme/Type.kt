package com.erman.percentagecalculator.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.erman.percentagecalculator.R

val PlusJakartaSans =
    FontFamily(
        Font(R.font.pjs_light, FontWeight.Light),
        Font(R.font.pjs_regular, FontWeight.Normal),
        Font(R.font.pjs_medium, FontWeight.Medium),
        Font(R.font.pjs_semibold, FontWeight.SemiBold),
        Font(R.font.pjs_bold, FontWeight.Bold),
    )

val Typography =
    Typography(
        defaultFontFamily = PlusJakartaSans,
        body1 =
            TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        body2 =
            TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
            ),
        h4 =
            TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 34.sp,
                letterSpacing = 0.25.sp,
            ),
        h6 =
            TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                letterSpacing = 0.15.sp,
            ),
        button =
            TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                letterSpacing = 0.5.sp,
            ),
    )
