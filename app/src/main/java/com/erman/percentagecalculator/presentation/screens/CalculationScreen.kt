package com.erman.percentagecalculator.presentation.screens

import android.content.ClipData
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.IntOffset
import com.erman.percentagecalculator.ERROR_INVALID_NUMBERS
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.RESULT_FORMAT
import com.erman.percentagecalculator.domain.CalculationEvent
import com.erman.percentagecalculator.domain.CalculationState
import com.erman.percentagecalculator.domain.batch.BatchEvent
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.presentation.AdSection
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.Dimens
import com.erman.percentagecalculator.presentation.EmptyResultContent
import com.erman.percentagecalculator.presentation.ErrorResultContent
import com.erman.percentagecalculator.presentation.NumberInput
import com.erman.percentagecalculator.presentation.midTextResId
import com.erman.percentagecalculator.presentation.sanitizeNumberInput
import com.erman.percentagecalculator.presentation.secondaryResultLabel
import com.erman.percentagecalculator.presentation.showPercentInResult
import com.erman.percentagecalculator.presentation.showSecondaryResult
import com.erman.percentagecalculator.presentation.theme.Shapes
import com.erman.percentagecalculator.presentation.titleResId
import com.erman.percentagecalculator.presentation.viewModel.BatchViewModel
import com.erman.percentagecalculator.presentation.viewModel.PercentageCalculatorViewModel
import com.erman.percentagecalculator.domain.service.ShareService
import com.google.android.gms.ads.AdSize
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.util.Locale
import kotlin.math.roundToInt

private const val SHAKE_ITERATIONS = 3
private const val SHAKE_OFFSET = 10f
private const val SHAKE_STIFFNESS = 10000f

private val BATCH_EXCLUDED_OPERATIONS =
    setOf(
        Operation.GPA_CONVERTER,
        Operation.TIP_CALCULATOR,
        Operation.COMPOUND_INTEREST,
    )

@Composable
fun CalculationScreen(
    operation: Operation,
    initialInputs: List<String>? = null,
    onBackClick: () -> Unit,
    onHistoryClick: () -> Unit,
) {
    val viewModel: PercentageCalculatorViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboard.current
    val shareService: ShareService = koinInject()
    val context = androidx.compose.ui.platform.LocalContext.current
    val copiedFormat = stringResource(R.string.copied)
    val resultLabel = stringResource(R.string.result)
    var isBatchMode by remember { mutableStateOf(false) }

    val batchViewModel: BatchViewModel = koinViewModel()
    val batchState by batchViewModel.state.collectAsState()
    val supportsBatch = operation !in BATCH_EXCLUDED_OPERATIONS

    LaunchedEffect(initialInputs) {
        initialInputs?.let { inputs ->
            inputs.getOrNull(0)?.let { viewModel.dispatch(CalculationEvent.UpdateFirstInput(it)) }
            inputs.getOrNull(1)?.let { viewModel.dispatch(CalculationEvent.UpdateSecondInput(it)) }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.navigationBarsPadding(),
            )
        },
        topBar = {
            AppBar(
                titleText = stringResource(operation.titleResId),
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
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).navigationBarsPadding()) {
            Box(modifier = Modifier.weight(1F)) {
                if (isBatchMode && supportsBatch) {
                    BatchModeContent(
                        operation = operation,
                        batchState = batchState,
                        onToggleBatch = { isBatchMode = false },
                        onFixedInputChange = { value ->
                            batchViewModel.dispatch(BatchEvent.UpdateFixedInput(sanitizeNumberInput(value)))
                        },
                        onBatchInputChange = { index, value ->
                            batchViewModel.dispatch(BatchEvent.UpdateBatchInput(index, sanitizeNumberInput(value)))
                        },
                        onAddInput = { batchViewModel.dispatch(BatchEvent.AddBatchInput()) },
                        onRemoveInput = { index -> batchViewModel.dispatch(BatchEvent.RemoveBatchInput(index)) },
                        onCalculate = { batchViewModel.dispatch(BatchEvent.Calculate) },
                    )
                } else {
                    NormalModeContent(
                        state = state,
                        operation = operation,
                        supportsBatch = supportsBatch,
                        onToggleBatch = {
                            isBatchMode = true
                            batchViewModel.dispatch(BatchEvent.SelectOperation(operation))
                            batchViewModel.dispatch(BatchEvent.UpdateFixedInput(state.firstInput))
                        },
                        onFirstValueChange = {
                            viewModel.dispatch(CalculationEvent.UpdateFirstInput(sanitizeNumberInput(it)))
                        },
                        onSecondValueChange = {
                            viewModel.dispatch(CalculationEvent.UpdateSecondInput(sanitizeNumberInput(it)))
                        },
                        onCalculate = {
                            dispatchCalculation(viewModel, operation, state.firstInput, state.secondInput)
                        },
                        onCopyResult = { result ->
                            val clipData = ClipData.newPlainText(resultLabel, AnnotatedString(result))
                            coroutineScope.launch {
                                clipboardManager.setClipEntry(clipData.toClipEntry())
                            }
                            val copiedMessage = String.format(copiedFormat, result)
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(message = copiedMessage)
                            }
                        },
                        onShareResult = { result ->
                            shareService.shareResult(context, result)
                        },
                    )
                }
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
private fun NormalModeContent(
    state: CalculationState,
    operation: Operation,
    supportsBatch: Boolean,
    onToggleBatch: () -> Unit,
    onFirstValueChange: (String) -> Unit,
    onSecondValueChange: (String) -> Unit,
    onCalculate: () -> Unit,
    onCopyResult: (String) -> Unit,
    onShareResult: (String) -> Unit,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        if (supportsBatch) {
            BatchModeToggle(isBatchMode = false, onToggle = onToggleBatch)
        }

        CalculationInputForm(
            modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMd),
            firstValue = state.firstInput,
            secondValue = state.secondInput,
            midText = stringResource(operation.midTextResId),
            showSecondInput = operation != Operation.GPA_CONVERTER,
            hasError = state.error != null,
            onFirstValueChange = onFirstValueChange,
            onSecondValueChange = onSecondValueChange,
            onCalculate = onCalculate,
        )

        ResultCard(
            modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMd),
            result =
                state.result?.let { result ->
                    val formatted = String.format(Locale.US, RESULT_FORMAT, result)
                    if (operation.showPercentInResult) {
                        formatted + stringResource(R.string.percent)
                    } else {
                        formatted
                    }
                },
            secondaryResult =
                if (operation.showSecondaryResult && state.secondaryResult != null) {
                    "${secondaryResultLabel(operation)} ${RESULT_FORMAT.format(state.secondaryResult)}"
                } else {
                    null
                },
            error = state.error,
            onCopyResult = onCopyResult,
            onShareResult = onShareResult,
        )
    }
}

private fun createCalculationEvent(
    operation: Operation,
    firstValue: Double,
    secondValue: Double,
): CalculationEvent {
    return when (operation) {
        Operation.FIND_PERCENTAGE -> CalculationEvent.CalculatePercentage(firstValue, secondValue)
        Operation.PERCENTAGE_OF_VALUE -> CalculationEvent.PercentageOfValue(firstValue, secondValue)
        Operation.INCREASE_PERCENTAGE -> CalculationEvent.IncreasePercentage(firstValue, secondValue)
        Operation.DECREASE_PERCENTAGE -> CalculationEvent.DecreasePercentage(firstValue, secondValue)
        Operation.PERCENTAGE_CHANGE -> CalculationEvent.PercentageChange(firstValue, secondValue)
        Operation.FRACTION_TO_PERCENTAGE -> CalculationEvent.FractionToPercentage(firstValue, secondValue)
        Operation.DISCOUNT -> CalculationEvent.CalculateDiscount(firstValue, secondValue)
        Operation.MARKUP -> CalculationEvent.CalculateMarkup(firstValue, secondValue)
        Operation.TAX -> CalculationEvent.CalculateTax(firstValue, secondValue)
        Operation.GPA_CONVERTER -> CalculationEvent.CalculateGpa(firstValue)
        Operation.TIP_CALCULATOR,
        Operation.COMPOUND_INTEREST,
        -> error("Handled by dedicated screens")
    }
}

private fun dispatchCalculation(
    viewModel: PercentageCalculatorViewModel,
    operation: Operation,
    firstValue: String,
    secondValue: String,
) {
    try {
        viewModel.dispatch(
            createCalculationEvent(
                operation,
                firstValue.toDouble(),
                if (operation == Operation.GPA_CONVERTER) 0.0 else secondValue.toDouble(),
            ),
        )
    } catch (_: NumberFormatException) {
        viewModel.dispatch(CalculationEvent.CalculationFailed(ERROR_INVALID_NUMBERS))
    }
}

@Suppress("LongParameterList", "LongMethod")
@Composable
private fun CalculationInputForm(
    modifier: Modifier = Modifier,
    firstValue: String,
    secondValue: String,
    midText: String,
    showSecondInput: Boolean = true,
    hasError: Boolean,
    onFirstValueChange: (String) -> Unit,
    onSecondValueChange: (String) -> Unit,
    onCalculate: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(hasError) {
        if (hasError) {
            repeat(SHAKE_ITERATIONS) {
                shakeOffset.animateTo(
                    SHAKE_OFFSET,
                    animationSpec = spring(stiffness = SHAKE_STIFFNESS),
                )
                shakeOffset.animateTo(
                    -SHAKE_OFFSET,
                    animationSpec = spring(stiffness = SHAKE_STIFFNESS),
                )
            }
            shakeOffset.animateTo(0f, animationSpec = spring(stiffness = SHAKE_STIFFNESS))
        }
    }

    val onDone = {
        keyboardController?.hide()
        onCalculate()
    }

    Surface(
        modifier = modifier.offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
        shape = Shapes.medium,
        elevation = Dimens.elevationMd,
    ) {
        Column(modifier = Modifier.padding(Dimens.spacingMd)) {
            NumberInput(
                modifier = Modifier.fillMaxWidth(),
                value = firstValue,
                label = stringResource(id = R.string.value),
                onValueChange = onFirstValueChange,
                onDone = onDone,
            )
            if (showSecondInput) {
                Spacer(modifier = Modifier.height(Dimens.spacingMd))
                Text(
                    text = midText,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(Dimens.spacingMd))
                NumberInput(
                    modifier = Modifier.fillMaxWidth(),
                    value = secondValue,
                    label = stringResource(id = R.string.value),
                    onValueChange = onSecondValueChange,
                    onDone = onDone,
                )
            }
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
                Text(text = stringResource(id = R.string.calculate))
            }
        }
    }
}

@Composable
private fun ResultCard(
    modifier: Modifier = Modifier,
    result: String?,
    secondaryResult: String? = null,
    error: String?,
    onCopyResult: (String) -> Unit,
    onShareResult: (String) -> Unit,
) {
    Surface(modifier = modifier, shape = Shapes.medium, elevation = Dimens.elevationMd) {
        when {
            error != null -> ErrorResultContent(error = error)
            result != null ->
                SuccessContent(
                    result = result,
                    secondaryResult = secondaryResult,
                    onCopyResult = onCopyResult,
                    onShareResult = onShareResult,
                )
            else -> EmptyResultContent()
        }
    }
}

@Composable
private fun SuccessContent(
    result: String,
    secondaryResult: String? = null,
    onCopyResult: (String) -> Unit,
    onShareResult: (String) -> Unit,
) {
    Row(
        modifier = Modifier.padding(vertical = Dimens.spacingSm, horizontal = Dimens.spacingMd),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = stringResource(id = R.string.result),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )
            Text(
                text = result,
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            )
            if (secondaryResult != null) {
                Text(
                    text = secondaryResult,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                )
            }
        }
        IconButton(
            onClick = { onShareResult(result) },
            modifier = Modifier.size(Dimens.touchTarget),
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(id = R.string.share),
            )
        }
        IconButton(
            onClick = { onCopyResult(result) },
            modifier = Modifier.size(Dimens.touchTarget),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy),
                contentDescription = stringResource(id = R.string.copy),
            )
        }
    }
}
