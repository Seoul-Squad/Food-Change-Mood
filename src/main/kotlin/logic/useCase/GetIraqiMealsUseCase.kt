package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.Constants.IRAQ_NAME
import logic.utils.NoIraqiMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GetIraqiMealsUseCase(
    private val mealRepository: MealRepository
) {
    fun getAllIraqMeals(): Result<List<Meal>> {
        return mealRepository.getAllMeals()
            .filter(::isIraqMeal)
            .takeIf { it.isNotEmpty() }
            ?.let {
                Result.success(it)
            } ?: Result.failure(NoIraqiMealsFoundException("No Iraqi meals found"))
    }

    private fun isIraqMeal(meal: Meal) =
        meal.tags.any { it.equals(IRAQ_NAME, ignoreCase = true) } ||
                meal.description?.contains(IRAQ_NAME, ignoreCase = true) ?: false
}