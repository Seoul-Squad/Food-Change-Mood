package org.seoulsquad.presentation

import logic.useCase.GetMealsByCaloriesAndProteinUseCase
import logic.utils.NoMealsFoundException
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer


class MealsByCaloriesAndProteinUi(
    private val getMealsByCaloriesAndProtein: GetMealsByCaloriesAndProteinUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {
    private val quitCommands = setOf("quit", "exit", "q")

    fun startGetMealsByCaloriesAndProtein() {
        viewer.display("=== Meal Nutrition Filter ===")
        viewer.display("(Enter 'quit' at any time to exit)")

        while (true) {
            val targetCalories = readValidatedDoubleOrQuit("Enter desired calories") ?: return
            val targetProtein = readValidatedDoubleOrQuit("Enter desired protein (g)") ?: return

            showMeals(targetCalories, targetProtein)

            if (isUserQuit()) return
            }
    }

    private fun readValidatedDoubleOrQuit(inputMessage: String): Double? {
        while (true) {
            viewer.display("$inputMessage: ")
            val input = reader.readString().trim().lowercase()

            when {
                input in quitCommands -> return null
                input.toDoubleOrNull() == null -> viewer.display("❌ Please enter a number")
                input.toDouble() <= 0 -> viewer.display("❌ Please enter a positive number")
                else -> return input.toDouble()
            }
        }
    }

    private fun showMeals(targetCalories: Double, targetProtein: Double) {
        try {
            val meals = getMealsByCaloriesAndProtein(targetCalories, targetProtein)
            viewer.display("✅ Found ${meals.size} meal(s) around ~${targetCalories} kcal and ~${targetProtein}g protein:")
            meals.forEach { viewer.display("- ${it.name}") }
        } catch (error: NoMealsFoundException) {
            viewer.display("⚠️ ${error.message}")
        }
    }

    private fun isUserQuit(): Boolean {
        viewer.display("\nSearch again? (Press Enter to continue or 'quit' to exit): ")
        return reader.readString().trim().lowercase() in quitCommands

    }
}