package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.Constants.Calories.HIGH_CALORIES
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GetMealsWithHighCaloriesUseCase(
    private val mealRepository: MealRepository
) {
    operator fun invoke(): Result<List<Meal>> =
        mealRepository
            .getAllMeals()
            .filter(::isMealWithHighCalories)
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoMealsFoundException())

    private fun isMealWithHighCalories(meal: Meal): Boolean =
        meal.nutrition.calories > HIGH_CALORIES
}