package org.seoulsquad.logic.utils


fun List<Double>.veryLowNutritionValue(percentage: Double): Double {
    val index = (percentage * (this.size - 1)).toInt()
    return this[index]
}