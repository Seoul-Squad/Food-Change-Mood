package org.seoulsquad.logic.use_case

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
        meal.tags.contains("sweet") && meal.ingredients.any { it.contains("egg", ignoreCase = true) }
}
