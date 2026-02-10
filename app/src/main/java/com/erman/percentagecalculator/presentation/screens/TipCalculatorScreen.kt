package com.erman.percentagecalculator.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.RESULT_FORMAT
import com.erman.percentagecalculator.domain.tipcalculator.TipCalculatorEvent
import com.erman.percentagecalculator.domain.tipcalculator.TipCalculatorState
import com.erman.percentagecalculator.presentation.AdSection
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.Dimens
import com.erman.percentagecalculator.presentation.EmptyResultContent
import com.erman.percentagecalculator.presentation.ErrorResultContent
import com.erman.percentagecalculator.presentation.NumberInput
import com.erman.percentagecalculator.presentation.sanitizeNumberInput
import com.erman.percentagecalculator.presentation.theme.Shapes
import com.erman.percentagecalculator.presentation.viewModel.TipCalculatorViewModel
import com.erman.percentagecalculator.domain.service.ShareService
import com.google.android.gms.ads.AdSize
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.util.Locale

@Composable
fun TipCalculatorScreen(
    initialInputs: List<String>? = null,
    onBackClick: () -> Unit,
    onHistoryClick: () -> Unit,
) {
    val viewModel: TipCalculatorViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val shareService: ShareService = koinInject()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(initialInputs) {
        initialInputs?.let { inputs ->
            inputs.getOrNull(0)?.let { viewModel.dispatch(TipCalculatorEvent.UpdateBillAmount(it)) }
            inputs.getOrNull(1)?.let { viewModel.dispatch(TipCalculatorEvent.UpdateTipPercentage(it)) }
            inputs.getOrNull(2)?.let { viewModel.dispatch(TipCalculatorEvent.UpdateSplitCount(it)) }
        }
    }

    Scaffold(scaffoldState = rememberScaffoldState(), topBar = {
        AppBar(
            titleText = stringResource(R.string.tip_calculator),
            onBackClick = onBackClick,
            actions = {
                IconButton(onClick = onHistoryClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_history),
                        contentDescription = stringResource(R.string.history),
                        tint = MaterialTheme.colors.onPrimary,
                    )
                }
            },
        )
    }) { padding ->
        Column(modifier = Modifier.padding(padding).navigationBarsPadding()) {
            Box(modifier = Modifier.weight(1F)) {
                TipCalculatorContent(
                    state = state,
                    onBillAmountChange = {
                        viewModel.dispatch(TipCalculatorEvent.UpdateBillAmount(sanitizeNumberInput(it)))
                    },
                    onTipPercentageChange = {
                        viewModel.dispatch(TipCalculatorEvent.UpdateTipPercentage(sanitizeNumberInput(it)))
                    },
                    onSplitCountChange = {
                        viewModel.dispatch(TipCalculatorEvent.UpdateSplitCount(sanitizeNumberInput(it)))
                    },
                    onCalculate = { viewModel.dispatch(TipCalculatorEvent.Calculate) },
                    onShareResult = { text -> shareService.shareResult(context, text) },
                )
            }
            AdSection(
                modifier = Modifier.fillMaxWidth(),
                adSize = AdSize.FULL_BANNER,
                adUnitIdRes = R.string.calculation_ad_unit_id,
            )
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun TipCalculatorContent(
    state: TipCalculatorState,
    onBillAmountChange: (String) -> Unit,
    onTipPercentageChange: (String) -> Unit,
    onSplitCountChange: (String) -> Unit,
    onCalculate: () -> Unit,
    onShareResult: (String) -> Unit,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TipInputForm(
            modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMd),
            state = state,
            onBillAmountChange = onBillAmountChange,
            onTipPercentageChange = onTipPercentageChange,
            onSplitCountChange = onSplitCountChange,
            onCalculate = onCalculate,
        )
        TipResultCard(
            modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMd),
            state = state,
            onShareResult = onShareResult,
        )
    }
}

@Composable
private fun TipInputForm(
    modifier: Modifier = Modifier,
    state: TipCalculatorState,
    onBillAmountChange: (String) -> Unit,
    onTipPercentageChange: (String) -> Unit,
    onSplitCountChange: (String) -> Unit,
    onCalculate: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val onDone = {
        keyboardController?.hide()
        onCalculate()
    }

    Surface(modifier = modifier, shape = Shapes.medium, elevation = Dimens.elevationMd) {
        Column(modifier = Modifier.padding(Dimens.spacingMd)) {
            NumberInput(
                modifier = Modifier.fillMaxWidth(),
                value = state.billAmount,
                label = stringResource(R.string.bill_amount),
                onValueChange = onBillAmountChange,
                onDone = onDone,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            NumberInput(
                modifier = Modifier.fillMaxWidth(),
                value = state.tipPercentage,
                label = stringResource(R.string.tip_percentage),
                onValueChange = onTipPercentageChange,
                onDone = onDone,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            NumberInput(
                modifier = Modifier.fillMaxWidth(),
                value = state.splitCount,
                label = stringResource(R.string.split_between),
                onValueChange = onSplitCountChange,
                onDone = onDone,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingLg))
            Button(
                onClick = {
                    keyboardController?.hide()
                    onCalculate()
                },
                modifier = Modifier.fillMaxWidth().height(Dimens.buttonHeight),
                shape = Shapes.small,
                colors =
                    ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                    ),
            ) {
                Text(text = stringResource(R.string.calculate))
            }
        }
    }
}

@Composable
private fun TipResultCard(
    modifier: Modifier = Modifier,
    state: TipCalculatorState,
    onShareResult: (String) -> Unit,
) {
    Surface(modifier = modifier, shape = Shapes.medium, elevation = Dimens.elevationMd) {
        when {
            state.error != null -> ErrorResultContent(error = state.error)
            state.tipAmount != null ->
                TipSuccessContent(
                    state = state,
                    onShareResult = onShareResult,
                )
            else -> EmptyResultContent()
        }
    }
}

@Composable
private fun TipSuccessContent(
    state: TipCalculatorState,
    onShareResult: (String) -> Unit,
) {
    val tipLabel = stringResource(R.string.tip_amount)
    val totalLabel = stringResource(R.string.total_amount)
    val perPersonLabel = stringResource(R.string.per_person)

    Column(
        modifier = Modifier.padding(Dimens.spacingMd),
    ) {
        TipResultRow(
            label = tipLabel,
            value = formatCurrency(state.tipAmount),
        )
        Spacer(modifier = Modifier.height(Dimens.spacingSm))
        TipResultRow(
            label = totalLabel,
            value = formatCurrency(state.totalAmount),
        )
        Spacer(modifier = Modifier.height(Dimens.spacingSm))
        TipResultRow(
            label = perPersonLabel,
            value = formatCurrency(state.perPersonAmount),
        )
        Spacer(modifier = Modifier.height(Dimens.spacingSm))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(
                onClick = {
                    val text =
                        "$tipLabel: ${formatCurrency(state.tipAmount)}, " +
                            "$totalLabel: ${formatCurrency(state.totalAmount)}, " +
                            "$perPersonLabel: ${formatCurrency(state.perPersonAmount)}"
                    onShareResult(text)
                },
                modifier = Modifier.size(Dimens.touchTarget),
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.share),
                )
            }
        }
    }
}

@Composable
private fun TipResultRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
        )
    }
}

private fun formatCurrency(value: Double?): String {
    return value?.let { String.format(Locale.US, RESULT_FORMAT, it) } ?: ""
}
