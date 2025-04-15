package org.seoulsquad.logic.use_case

import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.model.Meal
import org.seoulsquad.logic.utils.Constants.IRAQNAME

class GetIraqiMealsUseCase(
    private val mealRepository: MealRepository
) {
    fun getAllIraqMeals(): List<Meal> {
        return mealRepository.getAllMeals().filter(::onlyIraqiMeals)
    }

    private fun onlyIraqiMeals(meal: Meal) =
        meal.tags.any { it.equals(IRAQNAME, ignoreCase = true) } ||
                meal.description?.contains(IRAQNAME, ignoreCase = true) ?: false
}