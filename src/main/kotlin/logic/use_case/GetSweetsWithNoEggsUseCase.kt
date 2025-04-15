package org.seoulsquad.logic.use_case

import org.seoulsquad.data.utils.Constants.Ingredients.INGREDIENT_EGG
import org.seoulsquad.data.utils.Constants.Tags.TAG_SWEET
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.model.Meal

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
        meal.tags.contains(TAG_SWEET) && meal.ingredients.any { it.contains(INGREDIENT_EGG, ignoreCase = true) }
}
