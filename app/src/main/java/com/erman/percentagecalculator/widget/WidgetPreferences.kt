package com.erman.percentagecalculator.widget

import android.content.Context
import com.erman.percentagecalculator.WIDGET_OPERATION_KEY_PREFIX
import com.erman.percentagecalculator.WIDGET_PREFS_NAME
import com.erman.percentagecalculator.domain.model.Operation

object WidgetPreferences {
    fun saveOperation(
        context: Context,
        appWidgetId: Int,
        operation: Operation,
    ) {
        context.getSharedPreferences(WIDGET_PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(WIDGET_OPERATION_KEY_PREFIX + appWidgetId, operation.name)
            .apply()
    }

    fun loadOperation(
        context: Context,
        appWidgetId: Int,
    ): Operation? {
        val name =
            context.getSharedPreferences(WIDGET_PREFS_NAME, Context.MODE_PRIVATE)
                .getString(WIDGET_OPERATION_KEY_PREFIX + appWidgetId, null)
        return name?.let {
            try {
                Operation.valueOf(it)
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }

    fun removeOperation(
        context: Context,
        appWidgetId: Int,
    ) {
        context.getSharedPreferences(WIDGET_PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(WIDGET_OPERATION_KEY_PREFIX + appWidgetId)
            .apply()
    }
}
