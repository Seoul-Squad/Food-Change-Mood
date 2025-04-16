package org.seoulsquad.logic.use_case

import org.seoulsquad.logic.repository.MealRepository
import data.model.Meal
import org.seoulsquad.logic.utils.NoMealsFoundException

class ExploreOtherCountriesFoodUseCase(
    private val mealRepository: MealRepository
) {
    fun findMealsByCountry(countryName: String,limit:Int=5): Result<List<Meal>> {
        val meals = mealRepository
            .getAllMeals()
            .filter { isMealFromCountry(it, countryName) }
            .shuffled()
            .take(limit)

        return meals
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoMealsFoundException("No meals found for country: $countryName"))
    }

    private fun isMealFromCountry(meal: Meal, countryName: String) =
        meal.name.contains(countryName, ignoreCase = true) ||
        meal.description?.contains(countryName, ignoreCase = true) ?: false ||
        meal.tags.contains(countryName)
}

