package org.seoulsquad.presentation

import logic.useCase.GetMealsByCaloriesAndProteinUseCase
import logic.utils.NoMealsFoundException

class MealsByCaloriesAndProteinUi(
        private val getMealsByCaloriesAndProtein: GetMealsByCaloriesAndProteinUseCase
) {
    fun startGetMealsByCaloriesAndProtein() {
        println("=== Meal Nutrition Filter ===")
        print("Enter target calories: ")
        val targetCalories = readLine()?.toIntOrNull() ?: 0

        print("Enter target protein (g): ")
        val targetProtein = readLine()?.toIntOrNull() ?: 0
    try {

        val meals = getMealsByCaloriesAndProtein.getMealsByCaloriesAndProtein(
            targetCalories = targetCalories,
            targetProtein = targetProtein,
        )
        println("The available meals with ~${targetCalories} calories and ~${targetProtein}g protein are: ${meals.size}")
        meals.forEach { println("- ${it.name}") }
    }
    catch (error: NoMealsFoundException){
    println(error.message)
    }
    }

}