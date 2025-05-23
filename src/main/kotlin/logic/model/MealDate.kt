package org.seoulsquad.logic.model

import kotlinx.datetime.LocalDate

data class MealDate(
    val id: Int,
    val nameOfMeal: String,
    val date: LocalDate
)
