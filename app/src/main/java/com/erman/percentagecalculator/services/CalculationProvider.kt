package com.erman.percentagecalculator.services

import com.erman.percentagecalculator.PERCENTAGE_ENTIRETY

class CalculationProvider : CalculationService {
    override fun findPercentage(val1: Double, val2: Double): Double {
        return (val1 / val2) * PERCENTAGE_ENTIRETY
    }
    override fun findPercentageOfValue(val1: Double, val2: Double): Double {
        return (val1 / PERCENTAGE_ENTIRETY) * val2
    }
    override fun increaseByPercentage(val1: Double, val2: Double): Double {
        return val1 + ((val1 / PERCENTAGE_ENTIRETY) * val2)
    }
    override fun decreaseByPercentage(val1: Double, val2: Double): Double {
        return val1 - ((val1 / PERCENTAGE_ENTIRETY) * val2)
    }

    override fun percentageChange(val1: Double, val2: Double): Double {
        return (((val2 - val1) / val1) * PERCENTAGE_ENTIRETY)
    }

    override fun fractionToPercentage(val1: Double, val2: Double): Double {
        return ((val1 / val2) * PERCENTAGE_ENTIRETY)
    }
}
