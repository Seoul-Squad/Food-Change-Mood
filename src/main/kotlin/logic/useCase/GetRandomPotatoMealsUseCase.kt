package logic.useCase

import logic.model.Meal
import org.seoulsquad.logic.repository.MealRepository
import logic.utils.Constants.POTATO_ONLY
import logic.utils.NoMealsFoundException

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
            ?: Result.failure(NoMealsFoundException("No meals found containing potato"))

    private fun hasPotato(meal: Meal): Boolean = meal.ingredients.any { it.contains(POTATO_ONLY , ignoreCase = true) }
}
