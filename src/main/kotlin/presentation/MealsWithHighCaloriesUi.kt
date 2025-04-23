package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetMealsWithHighCaloriesUseCase
import org.seoulsquad.presentation.utils.MealSuggestionUi

class MealsWithHighCaloriesUi(
    private val getMealsWithHighCaloriesUseCase: GetMealsWithHighCaloriesUseCase,
    private val mealSuggestionUi: MealSuggestionUi
) {
    fun getMealsWithHighCalories() {
        getMealsWithHighCaloriesUseCase()
            .onSuccess { mealsList ->
                mealSuggestionUi.suggestMeal(mealsList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }
}