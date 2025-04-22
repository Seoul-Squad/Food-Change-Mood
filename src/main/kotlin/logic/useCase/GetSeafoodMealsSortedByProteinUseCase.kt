package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.Constants.Tags.TAG_SEAFOOD
import logic.utils.NoIngredientFoundException
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GetSeafoodMealsSortedByProteinUseCase(
    private val mealRepo: MealRepository,
) {
    operator fun invoke(): Result<List<Meal>> =
        mealRepo
            .getAllMeals()
            .filter(::isSeafoodMeal)
            .sortedWith(comparator = compareByDescending<Meal> { it.nutrition.protein }.thenBy { it.name },)
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) } ?: Result.failure(NoMealsFoundException())

    private fun isSeafoodMeal(meal: Meal): Boolean = meal.tags.any { it.equals(other = TAG_SEAFOOD, ignoreCase = true) }
}
