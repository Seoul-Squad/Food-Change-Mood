package org.seoulsquad.presentation

import logic.useCase.GetKetoDietMealUseCase
import org.seoulsquad.presentation.utils.MealSuggestionUi

class KetoDietMealsUi(
    private val getKetoDietMealUseCase: GetKetoDietMealUseCase,
    private val mealSuggestionUi: MealSuggestionUi
) {
    fun startKetoDietFlow() {
        printKetoIntroMessage()
        getKetoMeals()
    }

    private fun printKetoIntroMessage() {
        println("Following a Keto diet? We’ve got some low-carb options for you!")
        println("You can like to see full details or dislike to get another meal.")
        println("Loading Keto meals, please wait...")
    }

    private fun getKetoMeals() {
        getKetoDietMealUseCase
            .getKetoDietMeal()
            .onSuccess { ketoList ->
                mealSuggestionUi.suggestMeal(ketoList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }
}