package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import org.seoulsquad.presentation.utils.MealPrinter

class IraqiMealsUi(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val mealPrinter: MealPrinter
) {
    fun startIraqiMealsFlow() {
        printIraqiMealsIntroductionMessage()
        getIraqiMeals()
    }

    private fun printIraqiMealsIntroductionMessage() {
        mealPrinter.display("Looking for an Iraqi meal? You're in the right place!")
        mealPrinter.display("Loading, Please wait...")
    }

    private fun getIraqiMeals() {
        getIraqiMealsUseCase()
            .onSuccess { mealsList ->
                mealsList.forEach { meal ->
                    mealPrinter.printFullMeal(meal)
                    println("\n---\n")
                }
            }.onFailure { exception ->
                mealPrinter.display("error: ${exception.message}")
            }
    }
}