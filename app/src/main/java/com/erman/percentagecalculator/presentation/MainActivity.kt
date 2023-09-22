package com.erman.percentagecalculator.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.erman.percentagecalculator.presentation.navigation.Navigation
import com.erman.percentagecalculator.presentation.theme.PercentageCalculatorTheme
import com.erman.percentagecalculator.presentation.viewModel.PercentageCalculatorViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: PercentageCalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = PercentageCalculatorViewModel()

        setContent {
            PercentageCalculatorTheme {
                Navigation(viewModel)
            }
        }
    }
}
