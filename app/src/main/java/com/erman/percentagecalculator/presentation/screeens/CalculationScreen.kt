package com.erman.percentagecalculator.presentation.screeens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.erman.percentagecalculator.BuildConfig
import com.erman.percentagecalculator.DECIMAL_FORMAT
import com.erman.percentagecalculator.INPUT_VAL_INITIAL_STATE
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.UNKNOWN_ERROR
import com.erman.percentagecalculator.domain.CalculationEvent
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.navigation.Operation
import com.erman.percentagecalculator.presentation.theme.Shapes
import com.erman.percentagecalculator.presentation.theme.Typography
import com.erman.percentagecalculator.presentation.viewModel.PercentageCalculatorViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CalculatePercentageScreen(
    navController: NavController,
    viewModel: PercentageCalculatorViewModel,
    operation: Operation
) {
    val state by viewModel.state.observeAsState(PercentageCalculatorViewModel.ViewState())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Scaffold(scaffoldState = scaffoldState, topBar = {
        AppBar(navController, getScreenTitle(operation))
    }) {
        val isCalculationDone = remember { mutableStateOf(false) }

        Column(modifier = Modifier.padding(it)) {
            Box(modifier = Modifier.weight(1F)) {
                Column {
                    InputSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        viewModel, operation
                    ) { firstValue: String, secondValue: String ->
                        isCalculationDone.value = true
                        try {
                            getEvent(operation, firstValue.toDouble(), secondValue.toDouble()).run {
                                viewModel.dispatch(this)
                            }
                        } catch (err: NumberFormatException) {
                            viewModel.dispatch(CalculationEvent.Error(err.message ?: UNKNOWN_ERROR))
                        }
                    }

                    val context = LocalContext.current
                    ResultSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        isCalculationDone = isCalculationDone.value,
                        getResultWithUnit(DECIMAL_FORMAT.format(state.result), operation),
                        errorMessage = state.error
                    ) { result ->
                        clipboardManager.setText(AnnotatedString(result))
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = context.resources.getString(R.string.copied)
                            )
                        }
                    }
                }
            }
            AdSection(Modifier.fillMaxWidth())
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun InputSection(
    modifier: Modifier = Modifier,
    viewModel: PercentageCalculatorViewModel,
    operation: Operation,
    onClick: (String, String) -> Unit
) {
    val firstValue = remember { mutableStateOf(INPUT_VAL_INITIAL_STATE) }
    val secondValue = remember { mutableStateOf(INPUT_VAL_INITIAL_STATE) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = modifier, shape = Shapes.medium, elevation = 8.dp) {
        Column(modifier = Modifier.padding(8.dp)) {
            TextInputField(
                modifier = Modifier.fillMaxWidth(),
                value = firstValue.value,
                onValueChanged = { newValue ->
                    firstValue.value = viewModel.sanitizeInput(newValue)
                },
                onDoneTapped = {
                    keyboardController?.hide()
                    onClick(firstValue.value, secondValue.value)
                }
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = getMidText(operation),
                style = Typography.h6,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            TextInputField(
                modifier = Modifier.fillMaxWidth(),
                value = secondValue.value,
                onValueChanged = { newValue ->
                    secondValue.value = viewModel.sanitizeInput(newValue)
                },
                onDoneTapped = {
                    keyboardController?.hide()
                    onClick(firstValue.value, secondValue.value)
                }
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                onClick = {
                    keyboardController?.hide()
                    onClick(firstValue.value, secondValue.value)
                }, modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.calculate))
            }
        }
    }
}

@Composable
private fun TextInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    onDoneTapped: () -> Unit
) {
    TextField(
        value = value,
        label = { Text(text = stringResource(id = R.string.value)) },
        placeholder = { Text(text = stringResource(id = R.string.number_placeholder)) },
        onValueChange = { textFieldValue ->
            onValueChanged(textFieldValue)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier,
        keyboardActions = KeyboardActions(onDone = {
            onDoneTapped()
        }),
        trailingIcon = {
            Icon(
                Icons.Default.Clear,
                contentDescription = stringResource(id = R.string.clear),
                modifier = Modifier.clickable {
                    onValueChanged(INPUT_VAL_INITIAL_STATE)
                }
            )
        }
    )
}

@Composable
private fun ResultSection(
    modifier: Modifier = Modifier,
    isCalculationDone: Boolean,
    result: String,
    errorMessage: String? = null,
    onResultClicked: (String) -> Unit
) {
    if (!isCalculationDone) {
        return
    }

    val isThereError = errorMessage != null
    val resultText = if (errorMessage != null) {
        stringResource(id = R.string.error) + errorMessage
    } else {
        stringResource(id = R.string.result) + result
    }

    Surface(modifier = modifier, shape = Shapes.medium, elevation = 8.dp) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = resultText, style = Typography.h6, modifier = Modifier.weight(1F))
            IconButton(onClick = {
                if (!isThereError) {
                    onResultClicked(result)
                }
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun AdSection(modifier: Modifier = Modifier) {
    AndroidView(modifier = modifier, factory = { context ->
        AdView(context).apply {
            setAdSize(AdSize.FULL_BANNER)
            adUnitId = if (BuildConfig.DEBUG) {
                context.getString(R.string.test_ad_unit_id)
            } else {
                context.getString(R.string.calculation_ad_unit_id)
            }
            loadAd(AdRequest.Builder().build())
        }
    })
}

@Composable
private fun getScreenTitle(operation: Operation): String {
    return when (operation) {
        Operation.FIND_PERCENTAGE -> stringResource(id = R.string.calculate_percentage)
        Operation.PERCENTAGE_OF_VALUE -> stringResource(id = R.string.calculate_percentage_of_value)
        Operation.INCREASE_PERCENTAGE -> stringResource(id = R.string.increase_value_by_percentage)
        Operation.DECREASE_PERCENTAGE -> stringResource(id = R.string.decrease_value_by_percentage)
        Operation.PERCENTAGE_CHANGE -> stringResource(id = R.string.calculate_percentage_change)
        Operation.FRACTION_TO_PERCENTAGE -> stringResource(id = R.string.fraction_to_percentage)
    }
}

private fun getEvent(
    operation: Operation,
    firstValue: Double,
    secondValue: Double
): CalculationEvent {
    return when (operation) {
        Operation.FIND_PERCENTAGE -> CalculationEvent.CalculatePercentage(firstValue, secondValue)
        Operation.PERCENTAGE_OF_VALUE -> CalculationEvent.PercentageOfValue(firstValue, secondValue)
        Operation.INCREASE_PERCENTAGE -> CalculationEvent.IncreasePercentage(
            firstValue, secondValue
        )
        Operation.DECREASE_PERCENTAGE -> CalculationEvent.DecreasePercentage(
            firstValue, secondValue
        )
        Operation.PERCENTAGE_CHANGE -> CalculationEvent.PercentageChange(firstValue, secondValue)
        Operation.FRACTION_TO_PERCENTAGE -> CalculationEvent.FractionToPercentage(
            firstValue, secondValue
        )
    }
}

@Composable
private fun getMidText(operation: Operation): String {
    return when (operation) {
        Operation.FIND_PERCENTAGE -> stringResource(id = R.string.out_of)
        Operation.PERCENTAGE_OF_VALUE -> stringResource(id = R.string.of)
        Operation.INCREASE_PERCENTAGE -> stringResource(id = R.string.by)
        Operation.DECREASE_PERCENTAGE -> stringResource(id = R.string.by)
        Operation.PERCENTAGE_CHANGE -> stringResource(id = R.string.to)
        Operation.FRACTION_TO_PERCENTAGE -> stringResource(id = R.string.divided_by)
    }
}

@Composable
private fun getResultWithUnit(resultText: String, operation: Operation): String {
    return when (operation) {
        Operation.INCREASE_PERCENTAGE, Operation.DECREASE_PERCENTAGE -> resultText
        else -> resultText + stringResource(id = R.string.percent)
    }
}
