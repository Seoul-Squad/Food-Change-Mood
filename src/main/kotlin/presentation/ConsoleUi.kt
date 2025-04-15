package org.seoulsquad.presentation

import org.seoulsquad.logic.use_case.GetSweetsWithNoEggsUseCase
import org.seoulsquad.model.Meal

class ConsoleUi(
    private val getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase,
) {
    fun startSweetsWithNoEggsFlow() {
        println("Looking for a sweet without eggs? You're in the right place!")
        println("Like to see more details, or dislike to get another suggestion.")
        println("Loading, Please wait...")
        getSweetsWithNoEggsUseCase
            .getSweetsWithNoEggs()
            .onSuccess { sweetsList ->
                suggestMeal(sweetsList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }

    private fun suggestMeal(meals: List<Meal>) {
        if (meals.isEmpty()) {
            println("We are out of meals for now!")
            return
        }

        val randomMeal = meals.random()
        printShortMeal(randomMeal)
        printLikeAndDislikeOptions()

        when (readln().toIntOrNull()) {
            1 -> {
                printFullMeal(randomMeal)
            }

            2 -> {
                suggestMeal(meals - randomMeal)
            }

            else -> {
                println("Please, enter a valid option!")
                suggestMeal(meals)
            }
        }
    }

    private fun printLikeAndDislikeOptions() {
        println("1. Like")
        println("2. Dislike")
    }

    private fun printShortMeal(meal: Meal) {
        println("\u001B[1mMeal: ${meal.name}\u001B[0m")
        meal.description.takeIf { !it.isNullOrBlank() }.run { println("$this") }
    }

    private fun printFullMeal(meal: Meal) {
        with(meal) {
            println("Meal: $name (ID: $id)")
            println("Time to Prepare: $minutes minutes")
            meal.description.takeIf { !it.isNullOrBlank() }.run { println("$this") }
            println("Ingredients ($numberOfIngredients):")
            ingredients.forEachIndexed { index, ingredient ->
                println("  ${index + 1}. $ingredient")
            }
            println("Steps ($numberOfSteps):")
            steps.forEachIndexed { index, step ->
                println("  Step ${index + 1}: $step")
            }
            println("Nutrition:")
            println("  - Calories: ${nutrition.calories} kcal")
            println("  - Total Fat: ${nutrition.totalFat} g")
            println("  - Saturated Fat: ${nutrition.saturatedFat} g")
            println("  - Sugar: ${nutrition.sugar} g")
            println("  - Sodium: ${nutrition.sodium} mg")
            println("  - Protein: ${nutrition.protein} g")
            println("  - Carbohydrates: ${nutrition.carbohydrates} g")
        }
    }
}
