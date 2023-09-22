package com.erman.percentagecalculator.presentation.screeens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.domain.CalculationEvent
import com.erman.percentagecalculator.presentation.theme.Shapes
import com.erman.percentagecalculator.presentation.theme.Typography
import com.erman.percentagecalculator.presentation.viewModel.PercentageCalculatorViewModel

@Composable
fun FindPercentageScreen(viewModel: PercentageCalculatorViewModel) {
    val state by viewModel.state.observeAsState(PercentageCalculatorViewModel.ViewState())
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState, topBar = {
        com.erman.percentagecalculator.presentation.AppBar(stringResource(id = R.string.find_percentage))
    }) {
        val isResultVisible = remember { mutableStateOf(false) }

        Column(modifier = Modifier.padding(it)) {
            InputSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                viewModel
            ) {
                isResultVisible.value = true
            }
            ResultSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                isResultVisible = isResultVisible.value,
                result = state.error?.let { errorMessage ->
                    stringResource(id = R.string.error) + errorMessage
                } ?: run { stringResource(id = R.string.result) + state.result.toString() }
            )
        }
    }
}

@Composable
private fun InputSection(
    modifier: Modifier = Modifier,
    viewModel: PercentageCalculatorViewModel,
    onClick: () -> Unit
) {
    val firstValue = remember { mutableStateOf("") }
    val secondValue = remember { mutableStateOf("") }

    Surface(
        modifier = modifier,
        shape = Shapes.medium,
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = firstValue.value,
                label = { Text(text = stringResource(id = R.string.value)) },
                placeholder = { Text(text = stringResource(id = R.string.number_placeholder)) },
                onValueChange = { textFieldValue -> firstValue.value = textFieldValue },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = "Out of:",
                style = Typography.h6,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            TextField(
                value = secondValue.value,
                label = { Text(text = stringResource(id = R.string.value)) },
                placeholder = { Text(text = stringResource(id = R.string.number_placeholder)) },
                onValueChange = { textFieldValue ->
                    secondValue.value = textFieldValue
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                onClick = {
                    try {
                        onClick()
                        viewModel.dispatch(
                            CalculationEvent.FindPercentage(
                                firstValue.value.toDouble(),
                                secondValue.value.toDouble()
                            )
                        )
                    } catch (err: NumberFormatException) {
                        viewModel.dispatch(CalculationEvent.Error(err.message ?: "-"))
                    }
                }, modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.calculate))
            }
        }
    }
}

@Composable
private fun ResultSection(modifier: Modifier = Modifier, isResultVisible: Boolean, result: String) {
    Surface(
        modifier = modifier,
        shape = Shapes.medium,
        elevation = 8.dp
    ) {
        if (isResultVisible) {
            Box(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
                Text(
                    text = result,
                    style = Typography.h6,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
