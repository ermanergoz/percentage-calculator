package com.erman.percentagecalculator.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.RESULT_FORMAT
import com.erman.percentagecalculator.domain.batch.BatchState
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.presentation.Dimens
import com.erman.percentagecalculator.presentation.EmptyResultContent
import com.erman.percentagecalculator.presentation.ErrorResultContent
import com.erman.percentagecalculator.presentation.NumberInput
import com.erman.percentagecalculator.presentation.midTextResId
import com.erman.percentagecalculator.presentation.secondaryResultLabel
import com.erman.percentagecalculator.presentation.showPercentInResult
import com.erman.percentagecalculator.presentation.theme.Shapes
import java.util.Locale

@Composable
fun BatchModeToggle(
    isBatchMode: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spacingMd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.batch_mode),
            style = MaterialTheme.typography.body1,
        )
        Switch(
            checked = isBatchMode,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary),
        )
    }
}

@Suppress("LongParameterList")
@Composable
fun BatchModeContent(
    operation: Operation,
    batchState: BatchState,
    onToggleBatch: () -> Unit,
    onFixedInputChange: (String) -> Unit,
    onBatchInputChange: (Int, String) -> Unit,
    onAddInput: () -> Unit,
    onRemoveInput: (Int) -> Unit,
    onCalculate: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        BatchModeToggle(isBatchMode = true, onToggle = onToggleBatch)

        Surface(
            modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMd),
            shape = Shapes.medium,
            elevation = Dimens.elevationMd,
        ) {
            Column(modifier = Modifier.padding(Dimens.spacingMd)) {
                NumberInput(
                    modifier = Modifier.fillMaxWidth(),
                    value = batchState.fixedInput,
                    label = stringResource(operation.midTextResId),
                    onValueChange = onFixedInputChange,
                    onDone = {
                        keyboardController?.hide()
                        onCalculate()
                    },
                )

                Spacer(modifier = Modifier.height(Dimens.spacingMd))
                Text(
                    text = stringResource(R.string.value),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                batchState.batchInputs.forEachIndexed { index, input ->
                    Spacer(modifier = Modifier.height(Dimens.spacingSm))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NumberInput(
                            modifier = Modifier.weight(1F),
                            value = input,
                            label = stringResource(R.string.value),
                            onValueChange = { onBatchInputChange(index, it) },
                            onDone = {
                                keyboardController?.hide()
                                onCalculate()
                            },
                        )
                        if (batchState.batchInputs.size > 1) {
                            IconButton(onClick = { onRemoveInput(index) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colors.error,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.spacingSm))
                OutlinedButton(
                    onClick = onAddInput,
                    modifier = Modifier.fillMaxWidth(),
                    shape = Shapes.small,
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(Dimens.spacingSm))
                    Text(text = stringResource(R.string.add_value))
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
                    Text(text = stringResource(R.string.calculate))
                }
            }
        }

        BatchResultSection(
            modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMd),
            batchState = batchState,
            operation = operation,
        )
    }
}

@Composable
private fun BatchResultSection(
    modifier: Modifier = Modifier,
    batchState: BatchState,
    operation: Operation,
) {
    Surface(modifier = modifier, shape = Shapes.medium, elevation = Dimens.elevationMd) {
        when {
            batchState.error != null -> ErrorResultContent(error = batchState.error)
            batchState.results.isNotEmpty() ->
                Column(modifier = Modifier.padding(Dimens.spacingMd)) {
                    batchState.results.forEach { item ->
                        val formatted = String.format(Locale.US, RESULT_FORMAT, item.result)
                        val resultText = if (operation.showPercentInResult) "$formatted%" else formatted

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.spacingXs),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = item.input,
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                            )
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = resultText,
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.primary,
                                )
                                if (item.secondaryResult != null) {
                                    Text(
                                        text =
                                            "${secondaryResultLabel(operation)} " +
                                                RESULT_FORMAT.format(item.secondaryResult),
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                                    )
                                }
                            }
                        }
                    }
                }
            else -> EmptyResultContent()
        }
    }
}
