package org.seoulsquad.logic.use_case

import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.model.Meal
import org.seoulsquad.logic.utils.Constants.IRAQ_NAME
import org.seoulsquad.logic.utils.NoIraqiMealsFoundException

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