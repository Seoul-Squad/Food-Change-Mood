package org.seoulsquad.logic.use_case

import data.model.Meal
import org.seoulsquad.logic.repository.MealRepository

class GetTenRandomEasyMealsUseCase(private val mealRepository: MealRepository) {

    operator fun invoke(): List<Meal> {
        val easyMealsList =
            mealRepository.getAllMeals().filter(::isEasyMeal)

        return easyMealsList.shuffled().take(10)
    }

    private fun isEasyMeal(meal: Meal): Boolean =
        meal.minutes <= 30 && meal.ingredients.size <= 5 && meal.numberOfSteps <= 6
}