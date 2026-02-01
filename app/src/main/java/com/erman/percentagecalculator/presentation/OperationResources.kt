package com.erman.percentagecalculator.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.domain.model.Operation

val Operation.titleResId: Int
    @StringRes get() =
        when (this) {
            Operation.FIND_PERCENTAGE -> R.string.calculate_percentage
            Operation.PERCENTAGE_OF_VALUE -> R.string.calculate_percentage_of_value
            Operation.INCREASE_PERCENTAGE -> R.string.increase_value_by_percentage
            Operation.DECREASE_PERCENTAGE -> R.string.decrease_value_by_percentage
            Operation.PERCENTAGE_CHANGE -> R.string.calculate_percentage_change
            Operation.FRACTION_TO_PERCENTAGE -> R.string.fraction_to_percentage
            Operation.DISCOUNT -> R.string.calculate_discount
            Operation.MARKUP -> R.string.calculate_markup
            Operation.TAX -> R.string.calculate_tax
            Operation.GPA_CONVERTER -> R.string.convert_gpa
            Operation.TIP_CALCULATOR -> R.string.tip_calculator
            Operation.COMPOUND_INTEREST -> R.string.compound_interest
        }

val Operation.descriptionResId: Int
    @StringRes get() =
        when (this) {
            Operation.FIND_PERCENTAGE -> R.string.find_percentage_description
            Operation.PERCENTAGE_OF_VALUE -> R.string.find_percentage_of_value_description
            Operation.INCREASE_PERCENTAGE -> R.string.increase_value_by_percentage_description
            Operation.DECREASE_PERCENTAGE -> R.string.decrease_value_by_percentage_description
            Operation.PERCENTAGE_CHANGE -> R.string.find_percentage_change_description
            Operation.FRACTION_TO_PERCENTAGE -> R.string.fraction_to_percentage_description
            Operation.DISCOUNT -> R.string.discount_description
            Operation.MARKUP -> R.string.markup_description
            Operation.TAX -> R.string.tax_description
            Operation.GPA_CONVERTER -> R.string.gpa_description
            Operation.TIP_CALCULATOR -> R.string.tip_calculator_description
            Operation.COMPOUND_INTEREST -> R.string.compound_interest_description
        }

val Operation.midTextResId: Int
    @StringRes get() =
        when (this) {
            Operation.FIND_PERCENTAGE -> R.string.out_of
            Operation.PERCENTAGE_OF_VALUE -> R.string.of
            Operation.INCREASE_PERCENTAGE -> R.string.by
            Operation.DECREASE_PERCENTAGE -> R.string.by
            Operation.PERCENTAGE_CHANGE -> R.string.to
            Operation.FRACTION_TO_PERCENTAGE -> R.string.divided_by
            Operation.DISCOUNT -> R.string.discount_at
            Operation.MARKUP -> R.string.markup_by
            Operation.TAX -> R.string.tax_rate
            Operation.GPA_CONVERTER -> R.string.gpa_enter_percentage
            Operation.TIP_CALCULATOR -> R.string.bill_amount
            Operation.COMPOUND_INTEREST -> R.string.principal
        }

val Operation.iconResId: Int
    @DrawableRes get() =
        when (this) {
            Operation.FIND_PERCENTAGE -> R.drawable.ic_percent
            Operation.PERCENTAGE_OF_VALUE -> R.drawable.ic_pie
            Operation.INCREASE_PERCENTAGE -> R.drawable.ic_increase
            Operation.DECREASE_PERCENTAGE -> R.drawable.ic_decrease
            Operation.PERCENTAGE_CHANGE -> R.drawable.ic_change
            Operation.FRACTION_TO_PERCENTAGE -> R.drawable.ic_fraction
            Operation.DISCOUNT -> R.drawable.ic_discount
            Operation.MARKUP -> R.drawable.ic_markup
            Operation.TAX -> R.drawable.ic_tax
            Operation.GPA_CONVERTER -> R.drawable.ic_gpa
            Operation.TIP_CALCULATOR -> R.drawable.ic_tip
            Operation.COMPOUND_INTEREST -> R.drawable.ic_compound
        }

val Operation.showPercentInResult: Boolean
    get() =
        when (this) {
            Operation.FIND_PERCENTAGE,
            Operation.PERCENTAGE_CHANGE,
            Operation.FRACTION_TO_PERCENTAGE,
            -> true
            else -> false
        }

val Operation.showSecondaryResult: Boolean
    get() =
        when (this) {
            Operation.DISCOUNT,
            Operation.MARKUP,
            Operation.TAX,
            -> true
            else -> false
        }
