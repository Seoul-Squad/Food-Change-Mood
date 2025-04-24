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
        println("Looking for an Iraqi meal? You're in the right place!")
        println("Loading, Please wait...")
    }

    private fun getIraqiMeals() {
        getIraqiMealsUseCase
            .getAllIraqiMeals()
            .onSuccess { mealsList ->
                mealsList.forEach { meal ->
                    mealPrinter.printFullMeal(meal)
                    println("\n---\n")
                }
            }.onFailure { exception ->
                println(exception.message)
            }
    }
}