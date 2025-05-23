package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.Constants.IRAQ_NAME
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GetIraqiMealsUseCase(
    private val mealRepository: MealRepository
) {
    operator fun invoke(): Result<List<Meal>> {
        return mealRepository.getAllMeals()
            .filter(::isIraqMeal)
            .takeIf { it.isNotEmpty() }
            ?.let {
                Result.success(it)
            } ?: Result.failure(NoMealsFoundException())
    }

    private fun isIraqMeal(meal: Meal): Boolean {
        val tagMatch = meal.tags.any { it.equals(IRAQ_NAME, ignoreCase = true) }
        val descMatch = meal.description?.contains(IRAQ_NAME, ignoreCase = true) ?: false
        return tagMatch || descMatch
    }
}