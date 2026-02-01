package com.erman.percentagecalculator.domain.service

import com.erman.percentagecalculator.PERCENTAGE_ENTIRETY

class CalculationProvider : CalculationService {
    override fun findPercentage(
        part: Double,
        whole: Double,
    ): Double {
        require(whole != 0.0) { "Cannot divide by zero" }
        return (part / whole) * PERCENTAGE_ENTIRETY
    }

    override fun findPercentageOfValue(
        percentage: Double,
        value: Double,
    ): Double {
        return (percentage / PERCENTAGE_ENTIRETY) * value
    }

    override fun increaseByPercentage(
        value: Double,
        percentage: Double,
    ): Double {
        return value + ((value / PERCENTAGE_ENTIRETY) * percentage)
    }

    override fun decreaseByPercentage(
        value: Double,
        percentage: Double,
    ): Double {
        return value - ((value / PERCENTAGE_ENTIRETY) * percentage)
    }

    override fun percentageChange(
        oldValue: Double,
        newValue: Double,
    ): Double {
        require(oldValue != 0.0) { "Cannot divide by zero" }
        return ((newValue - oldValue) / oldValue) * PERCENTAGE_ENTIRETY
    }

    override fun fractionToPercentage(
        numerator: Double,
        denominator: Double,
    ): Double {
        require(denominator != 0.0) { "Cannot divide by zero" }
        return (numerator / denominator) * PERCENTAGE_ENTIRETY
    }

    override fun calculateDiscount(
        originalPrice: Double,
        discountPercent: Double,
    ): Pair<Double, Double> {
        val savings = (originalPrice / PERCENTAGE_ENTIRETY) * discountPercent
        val finalPrice = originalPrice - savings
        return Pair(finalPrice, savings)
    }

    override fun calculateMarkup(
        cost: Double,
        markupPercent: Double,
    ): Pair<Double, Double> {
        val profit = (cost / PERCENTAGE_ENTIRETY) * markupPercent
        val sellingPrice = cost + profit
        return Pair(sellingPrice, profit)
    }

    override fun calculateTax(
        amount: Double,
        taxRate: Double,
    ): Pair<Double, Double> {
        val taxAmount = (amount / PERCENTAGE_ENTIRETY) * taxRate
        val total = amount + taxAmount
        return Pair(total, taxAmount)
    }

    override fun calculateGpa(percentage: Double): Double {
        return when {
            percentage >= GPA_A_THRESHOLD -> GPA_A
            percentage >= GPA_B_THRESHOLD -> GPA_B
            percentage >= GPA_C_THRESHOLD -> GPA_C
            percentage >= GPA_D_THRESHOLD -> GPA_D
            else -> GPA_F
        }
    }

    companion object {
        private const val GPA_A_THRESHOLD = 90
        private const val GPA_B_THRESHOLD = 80
        private const val GPA_C_THRESHOLD = 70
        private const val GPA_D_THRESHOLD = 60
        private const val GPA_A = 4.0
        private const val GPA_B = 3.0
        private const val GPA_C = 2.0
        private const val GPA_D = 1.0
        private const val GPA_F = 0.0
    }
}
