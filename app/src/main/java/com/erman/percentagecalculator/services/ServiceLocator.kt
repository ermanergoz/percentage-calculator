package com.erman.percentagecalculator.services

object ServiceLocator {
    val calculationService: CalculationProvider by lazy { CalculationProvider() }
}
