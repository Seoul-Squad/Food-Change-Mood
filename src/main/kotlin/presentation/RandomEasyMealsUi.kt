package org.seoulsquad.presentation

import logic.model.Meal
import logic.useCase.GetRandomEasyMealsUseCase
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.SharedUi

class RandomEasyMealsUi(
    private val getRandomEasyMealsUseCase: GetRandomEasyMealsUseCase,
    private val viewer: Viewer
) {
    fun printRandomEasyMeals() {
        val result = getRandomEasyMealsUseCase()

        result
            .onSuccess { randomEasyMealsList ->
                randomEasyMealsList.forEach { meal ->
                    printFullMeal(meal)
                }
            }.onFailure { exception ->
                //exception.message?.let { viewer.display(it) }
                viewer.display(exception.message.toString())
            }
    }


    private fun printFullMeal(meal: Meal) {
        with(meal) {
            viewer.display("Meal: $name (ID: $id)")
            viewer.display("Time to Prepare: $preparationTimeInMinutes minutes")
            meal.description.takeIf { !it.isNullOrBlank() }?.run { viewer.display(this) }
            viewer.display("Ingredients ($numberOfIngredients):")
            ingredients.forEachIndexed { index, ingredient ->
                viewer.display("  ${index + 1}. $ingredient")
            }
            viewer.display("Steps ($numberOfSteps):")
            steps.forEachIndexed { index, step ->
                viewer.display("  Step ${index + 1}: $step")
            }
            viewer.display("Nutrition:")
            viewer.display("  - Calories: ${nutrition.calories} kcal")
            viewer.display("  - Total Fat: ${nutrition.totalFat} g")
            viewer.display("  - Saturated Fat: ${nutrition.saturatedFat} g")
            viewer.display("  - Sugar: ${nutrition.sugar} g")
            viewer.display("  - Sodium: ${nutrition.sodium} mg")
            viewer.display("  - Protein: ${nutrition.protein} g")
            viewer.display("  - Carbohydrates: ${nutrition.carbohydrates} g")
        }
        viewer.display("\n===========================================================\n")
    }
}