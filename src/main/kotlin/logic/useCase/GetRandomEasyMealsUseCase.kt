package org.seoulsquad.logic.use_case

import data.model.Meal
import org.seoulsquad.logic.repository.MealRepository

class GetRandomEasyMealsUseCase(private val mealRepository: MealRepository) {

    operator fun invoke(limit:Int = 10): Result<List<Meal>> {
        val easyMealsList =
            mealRepository.getAllMeals().filter(::isEasyMeal)

        return easyMealsList
            .shuffled()
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(easyMealsList.take(limit)) }
            ?: Result.failure(Exception("The file is empty"))
    }

    private fun isEasyMeal(meal: Meal): Boolean =
        meal.minutes <= 30 && meal.ingredients.size <= 5 && meal.steps.size <= 6
}