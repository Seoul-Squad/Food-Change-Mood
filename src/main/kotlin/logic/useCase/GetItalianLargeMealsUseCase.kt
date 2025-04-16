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
        (meal.tags.any { it.equals(Constants.Tags.ITALIAN_NAME, ignoreCase = true) } ||
                meal.description?.contains(Constants.Tags.ITALIAN_NAME, ignoreCase = true) ?: false) &&
                (meal.tags.any { it.equals(Constants.Tags.LARGE_GROUP_NAME, ignoreCase = true) })

}