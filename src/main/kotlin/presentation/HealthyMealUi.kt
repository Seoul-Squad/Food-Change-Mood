package org.seoulsquad.presentation

import logic.model.Meal
import org.seoulsquad.logic.useCase.GetHealthyFastFoodUseCase
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer

class HealthyMealUi(
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase,
    private val viewer: Viewer
) {
    fun presentHealthyMeal() {
        viewer.displayGreeting()
        getHealthyFastFoodUseCase
            .getFastHealthyMeals()
            .onSuccess { meals ->
                meals.forEach { viewer.displayMealDetails(it) }
            }
            .onFailure { error ->
                viewer.display(error.message ?: "Unknown error")
            }
    }

    private fun Viewer.displayGreeting() {
        display(
            "Hello, this is your list of healthy fast food " +
                    "that can be prepared in under 15 minutes with low nutrition values:"
        )
    }

    private fun Viewer.displayMealDetails(meal: Meal) = with(meal) {
        display("Meal: $name")
        display("Time to Prepare: $preparationTimeInMinutes minutes")
        display("Nutrition:")
        display("  - Calories: ${nutrition.calories} kcal")
        display("  - Total Fat: ${nutrition.totalFat} g")
        display("  - Saturated Fat: ${nutrition.saturatedFat} g")
        display("  - Sugar: ${nutrition.sugar} g")
        display("  - Sodium: ${nutrition.sodium} mg")
        display("  - Protein: ${nutrition.protein} g")
        display("  - Carbohydrates: ${nutrition.carbohydrates} g")
    }
}