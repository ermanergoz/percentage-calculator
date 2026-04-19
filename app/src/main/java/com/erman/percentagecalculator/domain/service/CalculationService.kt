package com.erman.percentagecalculator.domain.service

interface CalculationService {
    fun findPercentage(
        part: Double,
        whole: Double,
    ): Double

    fun findPercentageOfValue(
        percentage: Double,
        value: Double,
    ): Double

    fun increaseByPercentage(
        value: Double,
        percentage: Double,
    ): Double

    fun decreaseByPercentage(
        value: Double,
        percentage: Double,
    ): Double

    fun percentageChange(
        oldValue: Double,
        newValue: Double,
    ): Double

    fun fractionToPercentage(
        numerator: Double,
        denominator: Double,
    ): Double

    fun calculateDiscount(
        originalPrice: Double,
        discountPercent: Double,
    ): Pair<Double, Double>

    fun calculateMarkup(
        cost: Double,
        markupPercent: Double,
    ): Pair<Double, Double>

    fun calculateTax(
        amount: Double,
        taxRate: Double,
    ): Pair<Double, Double>

    fun calculateGpa(percentage: Double): Double
}
