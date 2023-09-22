package com.erman.percentagecalculator.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.erman.percentagecalculator.presentation.navigation.Navigation
import com.erman.percentagecalculator.presentation.theme.PercentageCalculatorTheme
import com.erman.percentagecalculator.presentation.viewModel.PercentageCalculatorViewModel
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: PercentageCalculatorViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewModel = PercentageCalculatorViewModel()
        MobileAds.initialize(this) {}

        setContent {
            PercentageCalculatorTheme {
                Navigation(viewModel)
            }
        }
    }
}
