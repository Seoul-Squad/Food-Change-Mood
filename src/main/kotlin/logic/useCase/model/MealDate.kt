package org.seoulsquad.logic.useCase.model

import kotlinx.datetime.LocalDate

data class MealDate(
    val id: Int,
    val nameOfMeal: String,
    val date: LocalDate
)
