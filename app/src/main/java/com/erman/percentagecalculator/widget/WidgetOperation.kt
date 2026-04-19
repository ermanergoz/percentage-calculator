package com.erman.percentagecalculator.widget

import android.content.Context
import androidx.glance.action.ActionParameters
import com.erman.percentagecalculator.EXTRA_WIDGET_OPERATION
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.domain.model.Operation

val widgetOperationKey = ActionParameters.Key<String>(EXTRA_WIDGET_OPERATION)

data class WidgetOperation(
    val label: String,
    val operation: Operation,
)

fun buildWidgetOperations(context: Context): List<WidgetOperation> =
    listOf(
        WidgetOperation(context.getString(R.string.widget_find_percent), Operation.FIND_PERCENTAGE),
        WidgetOperation(context.getString(R.string.widget_percent_of), Operation.PERCENTAGE_OF_VALUE),
        WidgetOperation(context.getString(R.string.widget_increase), Operation.INCREASE_PERCENTAGE),
        WidgetOperation(context.getString(R.string.widget_decrease), Operation.DECREASE_PERCENTAGE),
        WidgetOperation(context.getString(R.string.widget_change), Operation.PERCENTAGE_CHANGE),
        WidgetOperation(context.getString(R.string.widget_fraction), Operation.FRACTION_TO_PERCENTAGE),
        WidgetOperation(context.getString(R.string.widget_discount), Operation.DISCOUNT),
        WidgetOperation(context.getString(R.string.widget_markup), Operation.MARKUP),
        WidgetOperation(context.getString(R.string.widget_tax), Operation.TAX),
        WidgetOperation(context.getString(R.string.widget_gpa), Operation.GPA_CONVERTER),
        WidgetOperation(context.getString(R.string.widget_tip), Operation.TIP_CALCULATOR),
        WidgetOperation(context.getString(R.string.widget_compound), Operation.COMPOUND_INTEREST),
    )
