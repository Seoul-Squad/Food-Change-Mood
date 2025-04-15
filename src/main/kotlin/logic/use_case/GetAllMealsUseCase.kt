package org.seoulsquad.logic.use_case

import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.model.Meal

class GetAllMealsUseCase(private val repository: MealRepository) {
    operator fun invoke(): List<Meal> {
        return repository.getAllMeals()
    }
}