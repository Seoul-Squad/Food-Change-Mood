package logic.useCase

import logic.model.Meal
import logic.utils.Constants.Ingredients.INGREDIENT_EGG
import logic.utils.Constants.Tags.TAG_SWEET
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GetSweetsWithNoEggsUseCase(
    private val mealRepo: MealRepository,
) {
    operator fun invoke(): Result<List<Meal>> =
        mealRepo
            .getAllMeals()
            .filter(::isSweetWithNoEggs)
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoMealsFoundException())

    private fun isSweetWithNoEggs(meal: Meal): Boolean =
        meal.tags.contains(TAG_SWEET) && meal.ingredients.none { it.contains(INGREDIENT_EGG, ignoreCase = true) }
}
