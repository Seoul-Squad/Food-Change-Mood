package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.SharedUi

class IraqiMealsUi(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val viewer: Viewer,
    private val sharedUi: SharedUi
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
                    sharedUi.printFullMeal(meal)
                    viewer.display("\n---\n")
                }
            }.onFailure { exception ->
                viewer.display("error: ${exception.message}")
            }
    }
}