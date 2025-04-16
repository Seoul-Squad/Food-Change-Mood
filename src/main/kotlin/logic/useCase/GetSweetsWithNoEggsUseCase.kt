package logic.useCase

import logic.model.Meal
import logic.repository.MealRepository
import logic.utils.Constants.Ingredients.INGREDIENT_EGG
import logic.utils.Constants.Tags.TAG_SWEET

class GetSweetsWithNoEggsUseCase(
    private val mealRepo: MealRepository,
) {
    fun getSweetsWithNoEggs(): Result<List<Meal>> =
        mealRepo
            .getAllMeals()
            .filter(::isSweetWithNoEggs)
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No sweet meals without eggs found"))

    private fun isSweetWithNoEggs(meal: Meal): Boolean =
        meal.tags.contains(TAG_SWEET) && meal.ingredients.none { it.contains(INGREDIENT_EGG, ignoreCase = true) }
}
