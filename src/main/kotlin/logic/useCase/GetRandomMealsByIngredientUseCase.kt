package logic.useCase

import logic.model.Meal
import logic.utils.Constants.MAX_MEALS
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GetRandomMealsByIngredientUseCase(
    private val mealRepository: MealRepository,
) {
    operator fun invoke(
        ingredient: String,
        limit: Int = MAX_MEALS,
    ): Result<List<Meal>> =
        mealRepository
            .getAllMeals()
            .filter { hasPotato(it, ingredient) }
            .shuffled()
            .takeIf { it.isNotEmpty() }
            ?.let { meals ->
                Result.success(
                    meals.take(limit),
                )
            }
            ?: Result.failure(NoMealsFoundException())

    private fun hasPotato(
        meal: Meal,
        ingredient: String,
    ): Boolean = meal.ingredients.any { it.contains(ingredient, ignoreCase = true) }
}
