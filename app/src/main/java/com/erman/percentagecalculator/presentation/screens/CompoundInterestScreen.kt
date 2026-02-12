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
import com.erman.percentagecalculator.domain.compoundinterest.CompoundInterestEvent
import com.erman.percentagecalculator.domain.compoundinterest.CompoundInterestState
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.presentation.AdSection
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.Dimens
import com.erman.percentagecalculator.presentation.EmptyResultContent
import com.erman.percentagecalculator.presentation.ErrorResultContent
import com.erman.percentagecalculator.presentation.NumberInput
import com.erman.percentagecalculator.presentation.sanitizeNumberInput
import com.erman.percentagecalculator.presentation.theme.Shapes
import com.erman.percentagecalculator.presentation.titleResId
import com.erman.percentagecalculator.presentation.viewModel.CompoundInterestViewModel
import com.erman.percentagecalculator.domain.service.ShareService
import com.google.android.gms.ads.AdSize
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.util.Locale

private const val COMPOUNDS_PER_YEAR_INPUT_INDEX = 3

@Composable
fun CompoundInterestScreen(
    initialInputs: List<String>? = null,
    onBackClick: () -> Unit,
    onHistoryClick: () -> Unit,
) {
    val viewModel: CompoundInterestViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val shareService: ShareService = koinInject()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(initialInputs) {
        initialInputs?.let { inputs ->
            inputs.getOrNull(0)?.let { viewModel.dispatch(CompoundInterestEvent.UpdatePrincipal(it)) }
            inputs.getOrNull(1)?.let { viewModel.dispatch(CompoundInterestEvent.UpdateAnnualRate(it)) }
            inputs.getOrNull(2)?.let { viewModel.dispatch(CompoundInterestEvent.UpdateTime(it)) }
            inputs.getOrNull(COMPOUNDS_PER_YEAR_INPUT_INDEX)?.let {
                viewModel.dispatch(CompoundInterestEvent.UpdateCompoundsPerYear(it))
            }
        }
    }

    Scaffold(scaffoldState = rememberScaffoldState(), topBar = {
        AppBar(
            titleText = stringResource(Operation.COMPOUND_INTEREST.titleResId),
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
                CompoundInterestContent(
                    state = state,
                    onPrincipalChange = {
                        viewModel.dispatch(CompoundInterestEvent.UpdatePrincipal(sanitizeNumberInput(it)))
                    },
                    onAnnualRateChange = {
                        viewModel.dispatch(CompoundInterestEvent.UpdateAnnualRate(sanitizeNumberInput(it)))
                    },
                    onTimeChange = {
                        viewModel.dispatch(CompoundInterestEvent.UpdateTime(sanitizeNumberInput(it)))
                    },
                    onCompoundsPerYearChange = {
                        viewModel.dispatch(
                            CompoundInterestEvent.UpdateCompoundsPerYear(sanitizeNumberInput(it)),
                        )
                    },
                    onCalculate = { viewModel.dispatch(CompoundInterestEvent.Calculate) },
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
private fun CompoundInterestContent(
    state: CompoundInterestState,
    onPrincipalChange: (String) -> Unit,
    onAnnualRateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onCompoundsPerYearChange: (String) -> Unit,
    onCalculate: () -> Unit,
    onShareResult: (String) -> Unit,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        CompoundInterestInputForm(
            modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMd),
            state = state,
            onPrincipalChange = onPrincipalChange,
            onAnnualRateChange = onAnnualRateChange,
            onTimeChange = onTimeChange,
            onCompoundsPerYearChange = onCompoundsPerYearChange,
            onCalculate = onCalculate,
        )
        CompoundInterestResultCard(
            modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMd),
            state = state,
            onShareResult = onShareResult,
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun CompoundInterestInputForm(
    modifier: Modifier = Modifier,
    state: CompoundInterestState,
    onPrincipalChange: (String) -> Unit,
    onAnnualRateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onCompoundsPerYearChange: (String) -> Unit,
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
                value = state.principal,
                label = stringResource(R.string.principal),
                onValueChange = onPrincipalChange,
                onDone = onDone,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            NumberInput(
                modifier = Modifier.fillMaxWidth(),
                value = state.annualRate,
                label = stringResource(R.string.annual_rate),
                onValueChange = onAnnualRateChange,
                onDone = onDone,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            NumberInput(
                modifier = Modifier.fillMaxWidth(),
                value = state.timeInYears,
                label = stringResource(R.string.time_in_years),
                onValueChange = onTimeChange,
                onDone = onDone,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            NumberInput(
                modifier = Modifier.fillMaxWidth(),
                value = state.compoundsPerYear,
                label = stringResource(R.string.compounds_per_year),
                onValueChange = onCompoundsPerYearChange,
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
private fun CompoundInterestResultCard(
    modifier: Modifier = Modifier,
    state: CompoundInterestState,
    onShareResult: (String) -> Unit,
) {
    Surface(modifier = modifier, shape = Shapes.medium, elevation = Dimens.elevationMd) {
        when {
            state.error != null -> ErrorResultContent(error = state.error)
            state.futureValue != null ->
                CompoundInterestSuccessContent(
                    state = state,
                    onShareResult = onShareResult,
                )
            else -> EmptyResultContent()
        }
    }
}

@Composable
private fun CompoundInterestSuccessContent(
    state: CompoundInterestState,
    onShareResult: (String) -> Unit,
) {
    val futureLabel = stringResource(R.string.future_value)
    val interestLabel = stringResource(R.string.total_interest)

    Column(
        modifier = Modifier.padding(Dimens.spacingMd),
    ) {
        CompoundInterestResultRow(
            label = futureLabel,
            value = formatCompoundValue(state.futureValue),
        )
        Spacer(modifier = Modifier.height(Dimens.spacingSm))
        CompoundInterestResultRow(
            label = interestLabel,
            value = formatCompoundValue(state.totalInterest),
        )
        Spacer(modifier = Modifier.height(Dimens.spacingSm))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(
                onClick = {
                    val text =
                        "$futureLabel: ${formatCompoundValue(state.futureValue)}, " +
                            "$interestLabel: ${formatCompoundValue(state.totalInterest)}"
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
private fun CompoundInterestResultRow(
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

private fun formatCompoundValue(value: Double?): String {
    return value?.let { String.format(Locale.US, RESULT_FORMAT, it) } ?: ""
}
