package org.seoulsquad.logic.utils

import org.seoulsquad.logic.model.MealDate

fun List<Double>.veryLowNutritionValue(percentage: Double): Double {
    val index = (percentage * (this.size - 1)).toInt()
    return this[index]
}

fun List<MealDate>.isIdExistingAtList(id: String): Boolean = any { it.id == id.toInt() }

fun <T> List<T>.shuffledByIndices(limit: Int): List<T> {
    val indices = (this.indices).shuffled().take(limit)
    return indices.map { index -> this[index] }
}
