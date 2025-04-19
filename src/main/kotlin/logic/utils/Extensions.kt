package org.seoulsquad.logic.utils

import org.seoulsquad.logic.model.MealDate


fun List<Double>.veryLowNutritionValue(percentage: Double): Double {
    val index = (percentage * (this.size - 1)).toInt()
    return this[index]
}
fun List<MealDate>.isIdExistingAtList(id: String): Boolean {
    return any { it.id == id.toInt() }
}