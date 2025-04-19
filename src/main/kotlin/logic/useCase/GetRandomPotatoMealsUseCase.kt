package logic.useCase

import logic.model.Meal
import logic.utils.Constants.MAX_POTATO_MEALS
import logic.utils.Constants.POTATO_ONLY
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GetRandomPotatoMealsUseCase(
    private val mealRepository: MealRepository,
) {
    operator fun invoke(limit: Int = MAX_POTATO_MEALS): Result<List<Meal>> =
        mealRepository
            .getAllMeals()
            .filter(::hasPotato)
            .shuffled()
            .takeIf { it.isNotEmpty() }
            ?.let { meals ->
                Result.success(
                    meals.take(limit),
                )
            }
            ?: Result.failure(NoMealsFoundException("No meals found containing potato"))

    private fun hasPotato(meal: Meal): Boolean = meal.ingredients.any { it.contains(POTATO_ONLY, ignoreCase = true) }
}
