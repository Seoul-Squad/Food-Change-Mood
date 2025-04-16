package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.repository.MealRepository

class GetMealsWithHighCaloriesUseCase(
    private val mealRepository: MealRepository
) {
    operator fun invoke(): Result<List<Meal>> =
        mealRepository
            .getAllMeals()
            .filter(::isMealWithHighCalories)
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(Exception("No meals found"))

    private fun isMealWithHighCalories(meal: Meal): Boolean =
        meal.nutrition.calories > 700
}