package org.seoulsquad.logic.use_case

import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.Constants.POTATO_ONLY
import org.seoulsquad.model.Meal

class GetRandomPotatoMealsUseCase(
    private val mealRepository: MealRepository,
) {
    operator fun invoke(numberOfRandomPotatoMeals: Int = 10): Result<List<Meal>> =
        mealRepository
            .getAllMeals()
            .filter(::hasPotato)
            .shuffled()
            .takeIf { it.isNotEmpty() }
            ?.let { meals ->
                Result.success(
                    meals.take(numberOfRandomPotatoMeals),
                )
            }
            ?: Result.failure(Exception("No meals found containing potato"))

    private fun hasPotato(meal: Meal): Boolean = meal.ingredients.contains(POTATO_ONLY)
}
