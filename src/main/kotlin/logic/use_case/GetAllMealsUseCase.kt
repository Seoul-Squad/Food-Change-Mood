package org.seoulsquad.logic.use_case

import data.model.Meal
import org.seoulsquad.logic.repository.MealRepository

class GetAllMealsUseCase(private val repository: MealRepository) {
    operator fun invoke(): List<Meal> {
        return repository.getAllMeals()
    }
}