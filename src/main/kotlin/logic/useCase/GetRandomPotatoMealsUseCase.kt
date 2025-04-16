package logic.useCase

import logic.model.Meal
import logic.repository.MealRepository
import logic.utils.Constants.POTATO_ONLY

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
