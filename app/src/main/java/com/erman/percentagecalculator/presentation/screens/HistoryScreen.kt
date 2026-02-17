package com.erman.percentagecalculator.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.RESULT_FORMAT
import com.erman.percentagecalculator.domain.history.HistoryEvent
import com.erman.percentagecalculator.domain.model.HistoryItem
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.Dimens
import com.erman.percentagecalculator.presentation.iconResId
import com.erman.percentagecalculator.presentation.titleResId
import com.erman.percentagecalculator.presentation.viewModel.HistoryViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val HISTORY_INPUT_SEPARATOR = " , "
private const val HISTORY_RESULT_SEPARATOR = " = "
private const val HISTORY_DATE_FORMAT = "MMM d, HH:mm"
private const val DISMISS_ANIMATION_LABEL = "dismiss_bg"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    onEntryClick: (Operation, List<String>) -> Unit,
) {
    val viewModel: HistoryViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = {
        AppBar(
            titleText = stringResource(R.string.history),
            onBackClick = onBackClick,
            actions = {
                if (state.entries.isNotEmpty()) {
                    IconButton(onClick = { viewModel.dispatch(HistoryEvent.ClearAll) }) {
                        Icon(
                            imageVector = Icons.Filled.DeleteSweep,
                            contentDescription = stringResource(R.string.clear_all),
                            tint = MaterialTheme.colors.onPrimary,
                        )
                    }
                }
            },
        )
    }) { padding ->
        if (state.entries.isEmpty() && !state.isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.no_history),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                )
            }
        } else {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .navigationBarsPadding()
                        .padding(horizontal = Dimens.spacingMd),
                verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
            ) {
                item { Spacer(modifier = Modifier.height(Dimens.spacingSm)) }
                items(items = state.entries, key = { it.id }) { entry ->
                    val dismissState =
                        rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    viewModel.dispatch(HistoryEvent.DeleteEntry(entry.id))
                                    true
                                } else {
                                    false
                                }
                            },
                        )
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = {
                            val color by animateColorAsState(
                                targetValue =
                                    if (dismissState.dismissDirection != null) {
                                        MaterialTheme.colors.error
                                    } else {
                                        MaterialTheme.colors.surface
                                    },
                                label = DISMISS_ANIMATION_LABEL,
                            )
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .background(color, MaterialTheme.shapes.medium)
                                        .padding(horizontal = Dimens.spacingMd),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colors.onError,
                                )
                            }
                        },
                    ) {
                        HistoryCard(
                            entry = entry,
                            onClick = { onEntryClick(entry.operation, entry.inputs) },
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(Dimens.spacingSm)) }
            }
        }
    }
}

@Composable
private fun HistoryCard(
    entry: HistoryItem,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = Dimens.elevationSm,
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier.padding(Dimens.spacingMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMd),
        ) {
            Icon(
                painter = painterResource(id = entry.operation.iconResId),
                contentDescription = null,
                modifier = Modifier.size(Dimens.iconMd),
                tint = MaterialTheme.colors.primary,
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(entry.operation.titleResId),
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text =
                        entry.inputs.joinToString(HISTORY_INPUT_SEPARATOR) +
                            HISTORY_RESULT_SEPARATOR + formatResult(entry.result),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = formatTimestamp(entry.timestamp),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            )
        }
    }
}

private fun formatResult(result: Double): String {
    return if (result == result.toLong().toDouble()) {
        result.toLong().toString()
    } else {
        RESULT_FORMAT.format(result)
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat(HISTORY_DATE_FORMAT, Locale.getDefault())
    return sdf.format(Date(timestamp))
}
