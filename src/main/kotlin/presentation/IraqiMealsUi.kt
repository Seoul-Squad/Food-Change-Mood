package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import org.seoulsquad.presentation.utils.SharedFunctions

class IraqiMealsUi(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase
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
            .getAllIraqMeals()
            .onSuccess { mealsList ->
                mealsList.forEach { meal ->
                    SharedFunctions.printFullMeal(meal)
                    println("\n---\n")
                }
            }.onFailure { exception ->
                println(exception.message)
            }
    }
}