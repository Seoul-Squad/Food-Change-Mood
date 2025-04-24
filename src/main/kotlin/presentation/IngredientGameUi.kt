package org.seoulsquad.presentation

import logic.utils.InvalidNumberException
import org.seoulsquad.logic.model.IngredientGameStatus
import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.useCase.GetIngredientGameQuestionsUseCase
import org.seoulsquad.logic.useCase.GetIngredientGameStatusUseCase

class IngredientGameUi(
    private val getIngredientGameStatusUseCase: GetIngredientGameStatusUseCase,
    private val getIngredientGameQuestionsUseCase: GetIngredientGameQuestionsUseCase,
) {
    fun startIngredientGame() {
        var isPlaying = true
        while (isPlaying) {
            try {
                val questions = getIngredientGameQuestionsUseCase()
                for (question in questions) {
                    printQuestion(question)
                    val userInput = getUserAnswer(question.chooses)
                    val status =
                        checkGameStatus(
                            userInput,
                            question,
                        )
                    if (status.isGameOver) {
                        println("Your score is ${status.totalScore}")
                        break
                    }
                }
            } catch (e: InvalidNumberException) {
                println(e.message)
            }
            println("Game Over")
            askToPlayAgain().also { isPlaying = it }
        }
    }

    private fun checkGameStatus(
        userAnswer: Int,
        question: IngredientQuestion,
    ): IngredientGameStatus = getIngredientGameStatusUseCase(userAnswer, question)

    private fun getUserAnswer(chooses: List<Pair<Boolean, String>>): Int {
        val input =
            getUserInput().trim().toIntOrNull() ?: throw InvalidNumberException()
        val userAnswer = input.dec()

        return if (userAnswer in chooses.indices) {
            userAnswer
        } else {
            throw InvalidNumberException()
        }
    }

    private fun printQuestion(question: IngredientQuestion) {
        println("What is the correct ingredient for: ${question.mealName}")

        question.chooses.forEachIndexed { index, choose ->
            println("${index.inc()}- $choose")
        }
    }

    private fun askToPlayAgain(): Boolean { //
        println("Do you want to play again? (y/n)")
        while (true) {
            when (getUserInput().lowercase()) {
                "y" -> return true
                "n" -> return false
                else -> println("Invalid input. Please enter 'y' or 'n'")
            }
        }
    }

    private fun getUserInput() = readlnOrNull() ?: ""
}
