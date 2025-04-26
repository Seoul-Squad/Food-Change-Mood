package org.seoulsquad.presentation

import logic.utils.InvalidNumberException
import logic.utils.NotEnoughMealsFoundException
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
    fun startIngredientGameFlow() {
        var isPlaying = true
        while (isPlaying) {
            val questionsResult = getIngredientGameQuestionsUseCase()
            questionsResult
                .onSuccess { questions ->
                    startGame(questions)
                    askToPlayAgain().also { isPlaying = it }
                }.onFailure { exception ->
                    handleException(exception)
                    return@startIngredientGameFlow
                }
        }
    }

    private fun startGame(questions: List<IngredientQuestion>) {
        for (question in questions) {
            printQuestion(question)
            val status: IngredientGameStatus =
                updateGameStatus(
                    reader.readInt(),
                    question,
                ).getOrElse { exception ->
                    viewer.display("Game Over")
                    return handleException(exception)
                }
            if (status.isGameOver) {
                viewer.display("Game Over")
                viewer.display("Your score is ${status.totalScore}")
                break
            }
        }
    }

    private fun updateGameStatus(
        userAnswer: Int?,
        question: IngredientQuestion,
    ): Result<IngredientGameStatus> = getIngredientGameStatusUseCase(userAnswer, question)

    private fun printQuestion(question: IngredientQuestion) {
        viewer.display("What is the correct ingredient for: ${question.mealName}")

        question.chooses.forEachIndexed { index, choose ->
            viewer.display("${index.inc()}- $choose")
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is NotEnoughMealsFoundException -> viewer.display(exception.message)
            is InvalidNumberException -> viewer.display(exception.message)
            else -> viewer.display("An error occurred: ${exception.message}")
        }
    }

    private fun askToPlayAgain(): Boolean {
        viewer.display("Do you want to play again? (y/n).")
        while (true) {
            when (reader.readString().lowercase()) {
                "y" -> return true
                "n" -> return false
                else -> viewer.display("Invalid input. Please enter 'y' or 'n'")
            }
        }
    }
}
