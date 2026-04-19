package com.erman.percentagecalculator.presentation.screens

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.domain.model.ThemeMode
import com.erman.percentagecalculator.domain.settings.SettingsEvent
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.Dimens
import com.erman.percentagecalculator.presentation.viewModel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val viewModel: SettingsViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Scaffold(topBar = {
        AppBar(
            titleText = stringResource(id = R.string.settings),
            onBackClick = onBackClick,
        )
    }) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState()),
        ) {
            SectionHeader(
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.Sort,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                    )
                },
                title = stringResource(id = R.string.sort_by_usage),
            )
            Divider()
            ToggleRow(
                label = stringResource(id = R.string.sort_by_usage_description),
                isChecked = state.sortByUsage,
                onCheckedChange = { enabled ->
                    viewModel.dispatch(SettingsEvent.UpdateSortByUsage(enabled))
                },
            )

            SectionHeader(
                icon = { Icon(Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colors.primary) },
                title = stringResource(id = R.string.theme),
            )
            Divider()
            ThemeOption(
                label = stringResource(id = R.string.theme_system),
                isSelected = state.theme == ThemeMode.SYSTEM,
                onClick = {
                    viewModel.dispatch(SettingsEvent.UpdateTheme(ThemeMode.SYSTEM))
                    (context as? Activity)?.recreate()
                },
            )
            ThemeOption(
                label = stringResource(id = R.string.theme_light),
                isSelected = state.theme == ThemeMode.LIGHT,
                onClick = {
                    viewModel.dispatch(SettingsEvent.UpdateTheme(ThemeMode.LIGHT))
                    (context as? Activity)?.recreate()
                },
            )
            ThemeOption(
                label = stringResource(id = R.string.theme_dark),
                isSelected = state.theme == ThemeMode.DARK,
                onClick = {
                    viewModel.dispatch(SettingsEvent.UpdateTheme(ThemeMode.DARK))
                    (context as? Activity)?.recreate()
                },
            )

            SectionHeader(
                icon = { Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colors.primary) },
                title = stringResource(id = R.string.language),
            )
            Divider()
            SupportedLanguage.entries.forEach { language ->
                LanguageRow(
                    language = language,
                    isSelected = state.language == language.code,
                    onClick = {
                        viewModel.dispatch(SettingsEvent.UpdateLanguage(language.code))
                        (context as? Activity)?.recreate()
                    },
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    icon: @Composable () -> Unit,
    title: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimens.spacingMd,
                    vertical = Dimens.spacingSm,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(modifier = Modifier.width(Dimens.spacingSm))
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
        )
    }
}

@Composable
private fun ThemeOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(
                    horizontal = Dimens.spacingMd,
                    vertical = Dimens.spacingSm,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors =
                RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colors.primary,
                ),
        )
        Spacer(modifier = Modifier.width(Dimens.spacingSm))
        Text(
            text = label,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
private fun LanguageRow(
    language: SupportedLanguage,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(
                    horizontal = Dimens.spacingMd,
                    vertical = Dimens.spacingSm,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors =
                RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colors.primary,
                ),
        )
        Spacer(modifier = Modifier.width(Dimens.spacingSm))
        Text(
            text =
                if (language == SupportedLanguage.SYSTEM) {
                    stringResource(id = R.string.system_default)
                } else {
                    language.nativeName
                },
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
private fun ToggleRow(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!isChecked) }
                .padding(
                    horizontal = Dimens.spacingMd,
                    vertical = Dimens.spacingSm,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors =
                SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primary,
                ),
        )
    }
}
