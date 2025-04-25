package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter

class IraqiMealsUi(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val mealPrinter: MealPrinter,
    private val viewer: Viewer,
) {
    fun startIraqiMealsFlow() {
        printIraqiMealsIntroductionMessage()
        getIraqiMeals()
    }

    private fun printIraqiMealsIntroductionMessage() {
        viewer.display("Looking for an Iraqi meal? You're in the right place!")
        viewer.display("Loading, Please wait...")
    }

    private fun getIraqiMeals() {
        getIraqiMealsUseCase()
            .onSuccess { mealsList ->
                mealsList.forEach { meal ->
                    mealPrinter.printFullMeal(meal)
                    println("\n---\n")
                }
            }.onFailure { exception ->
                viewer.display("error: ${exception.message}")
            }
    }
}