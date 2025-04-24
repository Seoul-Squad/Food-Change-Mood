package org.seoulsquad.presentation

import GetItalianLargeMealsUseCase
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter

class ItalianLargeMealsUi(
    private val getItalianLargeMealsUseCase: GetItalianLargeMealsUseCase,
    private val mealPrinter: MealPrinter,
    private val viewer: Viewer
    ) {
    fun startItalianLargeMealsFlow() {
        printItalianLargeMealsIntroductionMessage()
        getItalianLargeMeals()
    }

    private fun printItalianLargeMealsIntroductionMessage() {
        viewer.display(
            """Are you a large group of friends traveling to Italy?
            | Do you want to share a meal?",
            |Here The suggestion
            """.trimMargin(),
        )
    }

    private fun getItalianLargeMeals() {
        getItalianLargeMealsUseCase()
            .onSuccess { italianMeals -> mealPrinter.printSearchResult(italianMeals) }
            .onFailure { e -> viewer.display("Error: ${e.message}") }
    }

}