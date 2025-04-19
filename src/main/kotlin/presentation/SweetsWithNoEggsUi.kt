package org.seoulsquad.presentation

import logic.useCase.GetSweetsWithNoEggsUseCase
import org.seoulsquad.presentation.utils.SharedUi

class SweetsWithNoEggsUi(
    private val getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase,
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
                SharedUi().suggestMeal(sweetsList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }



}