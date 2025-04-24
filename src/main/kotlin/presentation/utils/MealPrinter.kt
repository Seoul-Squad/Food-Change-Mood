package org.seoulsquad.presentation.utils

import logic.model.Meal
import org.seoulsquad.presentation.consolelIO.Viewer

class MealPrinter (
    private val viewer: Viewer
){
    fun printLikeAndDislikeOptions() {
        SuggestionFeedbackOption.entries.forEach {
            viewer.display("${it.ordinal}. ${it.title}")
        }
    }


    fun printShortMeal(meal: Meal) {
        viewer.display("\u001B[1mMeal: ${meal.name}\u001B[0m")
        meal.description.takeIf { !it.isNullOrBlank() }?.run { viewer.display(this) }
    }

    fun printFullMeal(meal: Meal) {
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


    fun printMeal(meal: Meal) {
        viewer.display(
            """
            -ID: ${meal.id}
                This recipe is called: ${meal.name},
            ${meal.description}
            
            ==============================================
            """.trimIndent(),
        )
    }
    fun printSearchResult(meals: List<Meal>) {
        meals.forEach { printMeal(it) }
    }
}