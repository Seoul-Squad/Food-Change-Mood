package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.repository.MealRepository
import logic.utils.Constants.Tags.TAG_SEAFOOD

class GetSortedSeafoodMealsUseCase(
    private val mealRepo: MealRepository,
) {
    operator fun invoke(comparator: Comparator<Meal>): Result<List<Meal>> =
        mealRepo
            .getAllMeals()
            .filter(::isSeafoodMeal)
            .sortedWith(comparator = comparator)
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No seafood meals found"))

    private fun isSeafoodMeal(meal: Meal): Boolean = meal.tags.any { it.equals(other = TAG_SEAFOOD, ignoreCase = true) }
}
