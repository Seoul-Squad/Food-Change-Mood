package org.seoulsquad.presentation

import logic.model.Meal
import org.seoulsquad.logic.useCase.GetHealthyFastFoodUseCase

class HealthyMealUi(
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase
    ) {
    fun presentHealthyMeal() {
        greetingMessageForGetHealthyMealFeature()
        getHealthyFastFoodUseCase
            .getFastHealthyMeals()
            .onSuccess { it.forEach { printHealthyMealsThanCanPreparedUnder15MinutesAndLowNutrition(it) } }
            .onFailure { println(it.message) }
    }

    private fun greetingMessageForGetHealthyMealFeature() {
        println("Hello this is your list of healthy fast food that can be prepared in under 15 with Low nutrition's")
    }

    private fun printHealthyMealsThanCanPreparedUnder15MinutesAndLowNutrition(meal: Meal) {
        with(meal) {
            println("Meal: $name")
            println("Time to Prepare: $minutes minutes")
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