package com.erman.percentagecalculator.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.presentation.theme.Shapes

@Composable
fun EmptyResultContent() {
    Row(
        modifier = Modifier.padding(Dimens.spacingLg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
            modifier = Modifier.size(Dimens.iconSm),
        )
        Spacer(modifier = Modifier.padding(horizontal = Dimens.spacingSm))
        Text(
            text = stringResource(id = R.string.empty_result_hint),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
        )
    }
}

@Composable
fun ErrorResultContent(error: String) {
    Row(
        modifier =
            Modifier
                .padding(Dimens.spacingMd)
                .semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = null,
            tint = MaterialTheme.colors.error,
            modifier = Modifier.size(Dimens.iconSm),
        )
        Spacer(modifier = Modifier.padding(horizontal = Dimens.spacingSm))
        Text(
            text = error,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.error,
        )
    }
}

@Composable
fun NumberInput(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        placeholder = { Text(text = stringResource(id = R.string.number_placeholder)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier,
        shape = Shapes.small,
        colors =
            TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = { onValueChange("") },
                    modifier = Modifier.size(Dimens.touchTarget),
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.clear),
                    )
                }
            }
        },
    )
}

private const val MULTIPLE_DOT_REPLACEMENT: String = "."
private const val CHAR_TO_BE_IGNORED: String = ","
private const val IGNORED_CHAR_REPLACEMENT: String = ""
private val CONSECUTIVE_DOTS_REGEX: Regex = Regex("\\.{2,}")

fun sanitizeNumberInput(input: String): String {
    return input.replace(CHAR_TO_BE_IGNORED, IGNORED_CHAR_REPLACEMENT)
        .replace(CONSECUTIVE_DOTS_REGEX, MULTIPLE_DOT_REPLACEMENT)
}
