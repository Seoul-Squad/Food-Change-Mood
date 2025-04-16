package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.repository.MealRepository
import logic.utils.Constants

class GetItalianLargeMealsUseCase(
    private val mealRepository: MealRepository
) {
    fun getItalianLargeMeals(): Result<List<Meal>> {
        return mealRepository.getAllMeals()
            .filter(::isItalianLargeMeal)
            .takeIf { it.isNotEmpty() }
            ?.let {
                Result.success(it)
            } ?: Result.failure(Exception("No Italian large meals found"))
    }

    private fun isItalianLargeMeal(meal: Meal) =
        (meal.tags.any { it.equals(Constants.Italian.ITALIAN_NAME, ignoreCase = true) } ||
                meal.description?.contains(Constants.Italian.ITALIAN_NAME, ignoreCase = true) ?: false) &&
                (meal.tags.any { it.equals(Constants.LargeGroup.LARGE_GROUP_NAME, ignoreCase = true) })

}