package org.seoulsquad.presentation

import logic.useCase.GetMealsByCaloriesAndProteinUseCase
import logic.utils.NoMealsFoundException

class MealsByCaloriesAndProteinUi(
    private val getMealsByCaloriesAndProtein: GetMealsByCaloriesAndProteinUseCase,
) {

    fun startGetMealsByCaloriesAndProtein() {
        println("=== Meal Nutrition Filter ===")
        while (true) {
            val targetCalories = readValidatedDouble("Enter target calories") ?: continue
            val targetProtein = readValidatedDouble("Enter target protein (g)") ?: continue

            val found = tryShowMeals(targetCalories, targetProtein)

            if (found) break
        }
    }
    private fun readValidatedDouble(prompt: String): Double? {
        print("$prompt: ")
        val input = readLine()
        val number = input?.toDoubleOrNull()
        return if (number == null || number <= 0) {
            println("❌ Invalid input. Please enter a positive number.")
            null
        } else {
            number
        }
    }

    private fun tryShowMeals(targetCalories: Double, targetProtein: Double): Boolean {
        return try {
            val meals = getMealsByCaloriesAndProtein(targetCalories, targetProtein)
            println("✅ Found ${meals.size} meal(s) around ~${targetCalories} kcal and ~${targetProtein}g protein:")
            meals.forEach { println("- ${it.name}") }
            true
        } catch (error: NoMealsFoundException) {
            println("⚠️ ${error.message}")
            false
        }
    }
}
