package org.seoulsquad.presentation

import logic.useCase.GetItalianLargeMealsUseCase
import org.seoulsquad.presentation.utils.SharedUi

class ItalianLargeMealsUi(
    private val getItalianLargeMealsUseCase: GetItalianLargeMealsUseCase
    ) {
    fun startItalianLargeMealsFlow() {
        printItalianLargeMealsIntroductionMessage()
        getItalianLargeMeals()
    }

    private fun printItalianLargeMealsIntroductionMessage() {
        println(
            """Are you a large group of friends traveling to Italy?
            | Do you want to share a meal?",
            |Here The suggestion
            """.trimMargin(),
        )
    }

    private fun getItalianLargeMeals() {
        getItalianLargeMealsUseCase()
            .onSuccess { italianMeals ->
                SharedUi().printSearchResult(italianMeals)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }

}