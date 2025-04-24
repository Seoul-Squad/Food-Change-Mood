package org.seoulsquad.presentation

import logic.utils.InvalidNumberException
import org.seoulsquad.logic.model.IngredientGameStatus
import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.useCase.GetIngredientGameQuestionsUseCase
import org.seoulsquad.logic.useCase.GetIngredientGameStatusUseCase
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer

class IngredientGameUi(
    private val getIngredientGameStatusUseCase: GetIngredientGameStatusUseCase,
    private val getIngredientGameQuestionsUseCase: GetIngredientGameQuestionsUseCase,
    private val viewer: Viewer,
    private val reader: Reader,
) {
    fun startIngredientGame() {
        var isPlaying = true
        while (isPlaying) {
            try {
                val questions = getIngredientGameQuestionsUseCase()
                for (question in questions) {
                    printQuestion(question)
                    val status =
                        updateGameStatus(
                            reader.readInt(),
                            question,
                        )
                    if (status.isGameOver) {
                        viewer.display("Your score is ${status.totalScore}")
                        break
                    }
                }
            } catch (e: InvalidNumberException) {
                viewer.display(e.message)
            }
            viewer.display("Game Over")
            askToPlayAgain().also { isPlaying = it }
        }
    }

    private fun updateGameStatus(
        userAnswer: Int?,
        question: IngredientQuestion,
    ): IngredientGameStatus = getIngredientGameStatusUseCase(userAnswer, question)

    private fun printQuestion(question: IngredientQuestion) {
        viewer.display("What is the correct ingredient for: ${question.mealName}")

        question.chooses.forEachIndexed { index, choose ->
            viewer.display("${index.inc()}- $choose")
        }
    }

    private fun askToPlayAgain(): Boolean { //
        viewer.display("Do you want to play again? (y/n)")
        while (true) {
            when (reader.readString().lowercase()) {
                "y" -> return true
                "n" -> return false
                else -> viewer.display("Invalid input. Please enter 'y' or 'n'")
            }
        }
    }
}
