package com.erman.percentagecalculator.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.presentation.MainActivity

class PercentageGlanceWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        provideContent {
            GlanceTheme {
                WidgetContent(context)
            }
        }
    }

    @Composable
    private fun WidgetContent(context: Context) {
        Column(
            modifier =
                GlanceModifier
                    .fillMaxWidth()
                    .background(WidgetColors.BACKGROUND)
                    .cornerRadius(16.dp)
                    .padding(8.dp),
        ) {
            WidgetHeader(context)
            Spacer(modifier = GlanceModifier.height(4.dp))
            OperationButtonGrid(context)
        }
    }

    @Composable
    private fun WidgetHeader(context: Context) {
        Text(
            text = context.getString(R.string.app_name),
            style =
                TextStyle(
                    color = ColorProvider(WidgetColors.TEXT_PRIMARY),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
            modifier = GlanceModifier.padding(horizontal = 4.dp),
        )
    }

    @Composable
    private fun OperationButtonGrid(context: Context) {
        val operations = buildWidgetOperations(context)
        val rows = operations.chunked(COLUMNS_PER_ROW)

        Column(
            modifier = GlanceModifier.fillMaxWidth(),
        ) {
            rows.forEachIndexed { index, row ->
                if (index > 0) {
                    Spacer(modifier = GlanceModifier.height(4.dp))
                }
                OperationRow(
                    operations = row,
                    modifier = GlanceModifier.fillMaxWidth(),
                )
            }
        }
    }

    @Composable
    private fun OperationRow(
        operations: List<WidgetOperation>,
        modifier: GlanceModifier = GlanceModifier,
    ) {
        Row(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            operations.forEachIndexed { index, widgetOperation ->
                if (index > 0) {
                    Spacer(modifier = GlanceModifier.width(4.dp))
                }
                OperationButton(
                    widgetOperation = widgetOperation,
                    modifier = GlanceModifier.defaultWeight(),
                )
            }
        }
    }

    @Composable
    private fun OperationButton(
        widgetOperation: WidgetOperation,
        modifier: GlanceModifier = GlanceModifier,
    ) {
        Column(
            modifier =
                modifier
                    .background(WidgetColors.BUTTON_BACKGROUND)
                    .cornerRadius(8.dp)
                    .padding(horizontal = 8.dp, vertical = 10.dp)
                    .clickable(
                        actionStartActivity<MainActivity>(
                            actionParametersOf(widgetOperationKey to widgetOperation.operation.name),
                        ),
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = widgetOperation.label,
                style =
                    TextStyle(
                        color = ColorProvider(WidgetColors.BUTTON_TEXT),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                maxLines = 1,
            )
        }
    }

    companion object {
        private const val COLUMNS_PER_ROW = 3
    }
}
