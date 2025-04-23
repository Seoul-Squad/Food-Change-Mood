package org.seoulsquad.presentation

import logic.useCase.GetSweetsWithNoEggsUseCase
import org.seoulsquad.presentation.utils.MealSuggestionUi

class SweetsWithNoEggsUi(
    private val getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase,
    private val mealSuggestionUi: MealSuggestionUi
) {
    fun startSweetsWithNoEggsFlow() {
        printSweetsWithNoEggsIntroductionMessage()
        getSweetsWithNoEggs()
    }

    private fun printSweetsWithNoEggsIntroductionMessage() {
        println("Looking for a sweet without eggs? You're in the right place!")
        println("Like to see more details, or dislike to get another suggestion.")
        println("Loading, Please wait...")
    }

    private fun getSweetsWithNoEggs() {
        getSweetsWithNoEggsUseCase()
            .onSuccess { sweetsList ->
                mealSuggestionUi.suggestMeal(sweetsList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }



}