package com.erman.percentagecalculator.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.erman.percentagecalculator.R

@Composable
fun AppBar(
    titleText: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    Surface(color = MaterialTheme.colors.primary) {
        Column {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            TopAppBar(
                title = {
                    Text(text = titleText)
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                navigationIcon =
                    onBackClick?.let {
                        {
                            IconButton(onClick = it) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(id = R.string.back),
                                )
                            }
                        }
                    },
                actions = { actions?.invoke(this) },
            )
        }
    }
}
