package com.erman.percentagecalculator.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.presentation.MainActivity
import com.erman.percentagecalculator.presentation.iconResId
import com.erman.percentagecalculator.presentation.titleResId

class SingleOperationGlanceWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val operation =
            WidgetPreferences.loadOperation(context, appWidgetId)
                ?: Operation.FIND_PERCENTAGE

        provideContent {
            GlanceTheme {
                SingleOperationContent(context, operation)
            }
        }
    }

    @Composable
    private fun SingleOperationContent(
        context: Context,
        operation: Operation,
    ) {
        Column(
            modifier =
                GlanceModifier
                    .fillMaxSize()
                    .background(WidgetColors.BUTTON_BACKGROUND)
                    .cornerRadius(16.dp)
                    .padding(8.dp)
                    .clickable(
                        actionStartActivity<MainActivity>(
                            actionParametersOf(widgetOperationKey to operation.name),
                        ),
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                provider = ImageProvider(operation.iconResId),
                contentDescription = null,
                modifier = GlanceModifier.size(28.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(ColorProvider(WidgetColors.BUTTON_TEXT)),
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = context.getString(operation.titleResId),
                style =
                    TextStyle(
                        color = ColorProvider(WidgetColors.BUTTON_TEXT),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = androidx.glance.text.TextAlign.Center,
                    ),
                maxLines = 2,
            )
        }
    }
}
