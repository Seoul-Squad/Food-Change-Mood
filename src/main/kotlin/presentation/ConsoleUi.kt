package org.seoulsquad.presentation

import org.seoulsquad.logic.use_case.GetKetoDietMealUseCase
import org.seoulsquad.model.Meal

class ConsoleUi(
    private val getKetoDietMealUseCase: GetKetoDietMealUseCase,
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
                suggestMeal(ketoList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }

    private fun suggestMeal(meals: List<Meal>) {
        if (meals.isEmpty()) {
            println("No more keto meals available!")
            return
        }
        val randomMeal = meals.random()
        printShortMeal(randomMeal)
        printLikeAndDislikeOptions()
        handleFeedback(randomMeal, meals)
    }

    private fun handleFeedback(
        randomMeal: Meal,
        meals: List<Meal>,
    ) {
        when (readln().toIntOrNull()) {
            FeedbackOption.LIKE.ordinal -> {
                printFullMeal(randomMeal)
            }

            FeedbackOption.DISLIKE.ordinal -> {
                suggestMeal(meals.minusElement(randomMeal))
            }

            else -> {
                println("Please enter a valid option!")
                suggestMeal(meals)
            }
        }
    }

    private fun printLikeAndDislikeOptions() {
        FeedbackOption.entries.forEach {
            println("${it.ordinal}. ${it.title}")
        }
    }

    private fun printShortMeal(meal: Meal) {
        println("\u001B[1mMeal: ${meal.name}\u001B[0m")
        meal.description.takeIf { !it.isNullOrBlank() }?.let { println(it) }
    }

    private fun printFullMeal(meal: Meal) {
        with(meal) {
            println("Meal: $name (ID: $id)")
            println("Time to Prepare: $minutes minutes")
            meal.description.takeIf { !it.isNullOrBlank() }?.let { println(it) }
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

    private enum class FeedbackOption(val title: String) {
        LIKE("I like it! Show me full details"),
        DISLIKE("Not interested. Show me another one"),
    }

}

