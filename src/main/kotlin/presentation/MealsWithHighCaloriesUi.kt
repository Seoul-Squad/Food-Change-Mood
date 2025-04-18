package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetMealsWithHighCaloriesUseCase
import org.seoulsquad.presentation.utils.SharedFunctions

class MealsWithHighCaloriesUi(
    private val getMealsWithHighCaloriesUseCase: GetMealsWithHighCaloriesUseCase
) {
    fun getMealsWithHighCalories() {
        getMealsWithHighCaloriesUseCase()
            .onSuccess { mealsList ->
                SharedFunctions.suggestMeal(mealsList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }
}