package com.erman.percentagecalculator.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.lifecycleScope
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.domain.repository.PreferencesRepository
import com.erman.percentagecalculator.presentation.Dimens
import com.erman.percentagecalculator.presentation.iconResId
import com.erman.percentagecalculator.presentation.theme.PercentageCalculatorTheme
import com.erman.percentagecalculator.presentation.theme.Shapes
import com.erman.percentagecalculator.presentation.titleResId
import com.erman.percentagecalculator.data.LocaleHelper
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private const val GRID_COLUMNS = 3

/**
 * Configuration activity for the single-operation widget.
 *
 * Displays a 3-column grid of all available operations. When the user selects one,
 * the operation is persisted via [WidgetPreferences], the widget is refreshed,
 * and the activity finishes with [RESULT_OK].
 *
 * Per Android widget guidelines, result is set to [RESULT_CANCELED] initially so
 * that cancelling the config screen does not add the widget to the home screen.
 */
class SingleOperationConfigActivity : ComponentActivity() {
    private val preferencesRepository: PreferencesRepository by inject()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Standard widget config pattern: cancel by default until the user confirms
        setResult(RESULT_CANCELED)

        val appWidgetId =
            intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID,
            )

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val themeMode = preferencesRepository.getTheme()

        setContent {
            PercentageCalculatorTheme(themeMode = themeMode) {
                ConfigScreen(
                    onOperationSelected = { operation ->
                        onOperationConfirmed(appWidgetId, operation)
                    },
                )
            }
        }
    }

    /**
     * Persists the chosen operation, triggers a widget update, and closes the activity.
     *
     * The Glance update must run in a coroutine because [GlanceAppWidgetManager.getGlanceIdBy]
     * is a suspending function.
     */
    private fun onOperationConfirmed(
        appWidgetId: Int,
        operation: Operation,
    ) {
        WidgetPreferences.saveOperation(this, appWidgetId, operation)

        lifecycleScope.launch {
            val glanceId =
                GlanceAppWidgetManager(this@SingleOperationConfigActivity)
                    .getGlanceIdBy(appWidgetId)
            SingleOperationGlanceWidget().update(this@SingleOperationConfigActivity, glanceId)

            setResult(RESULT_OK, intent)
            finish()
        }
    }
}

/**
 * Full-screen configuration UI showing a title and a scrollable grid of operation cards.
 */
@Composable
private fun ConfigScreen(onOperationSelected: (Operation) -> Unit) {
    val operations = Operation.entries

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(Dimens.spacingMd),
    ) {
        Text(
            text = stringResource(R.string.widget_select_operation),
            style = MaterialTheme.typography.h6,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.spacingMd),
            textAlign = TextAlign.Center,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMNS),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
        ) {
            items(operations) { operation ->
                OperationCard(
                    operation = operation,
                    onClick = { onOperationSelected(operation) },
                )
            }
        }
    }
}

/**
 * A tappable card showing the operation's icon and localized title.
 */
@Composable
private fun OperationCard(
    operation: Operation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .clickable { onClick() }
                .semantics { role = Role.Button },
        shape = Shapes.medium,
        elevation = Dimens.elevationSm,
    ) {
        Column(
            modifier = Modifier.padding(Dimens.spacingXs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(Dimens.spacingXs))

            Icon(
                imageVector = ImageVector.vectorResource(id = operation.iconResId),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(Dimens.iconMd),
            )

            Spacer(modifier = Modifier.height(Dimens.spacingXs))

            Text(
                text = stringResource(operation.titleResId),
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier.padding(
                        horizontal = Dimens.spacingXs,
                        vertical = Dimens.spacingXs,
                    ),
            )
        }
    }
}
