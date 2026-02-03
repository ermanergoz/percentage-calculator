package com.erman.percentagecalculator.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.domain.model.Operation

@Composable
fun secondaryResultLabel(operation: Operation): String {
    return when (operation) {
        Operation.DISCOUNT -> stringResource(R.string.savings)
        Operation.MARKUP -> stringResource(R.string.profit)
        Operation.TAX -> stringResource(R.string.tax_amount)
        else -> ""
    }
}
