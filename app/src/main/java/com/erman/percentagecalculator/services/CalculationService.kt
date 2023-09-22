package com.erman.percentagecalculator.services

interface CalculationService {
    fun findPercentage(val1: Double, val2: Double): Double
    fun findPercentageOfValue(val1: Double, val2: Double): Double
    fun increaseByPercentage(val1: Double, val2: Double): Double
    fun decreaseByPercentage(val1: Double, val2: Double): Double
    fun percentageChange(val1: Double, val2: Double): Double
    fun fractionToPercentage(val1: Double, val2: Double): Double
}
