package org.seoulsquad.presentation

import logic.utils.InvalidInputException
import org.seoulsquad.logic.model.IngredientGameStatus
import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.useCase.IngredientGameUseCase

class IngredientGameUi(
    private val ingredientGameUseCase: IngredientGameUseCase,
) {
    fun startIngredientGame() {
        var isPlaying = true
        while (isPlaying) {
            var totalScore = 0
            try {
                val questions = ingredientGameUseCase.getIngredientGameQuestions()
                for (question in questions) {
                    printQuestion(question)
                    val userInput = getUserAnswer(question.chooses)
                    val status = checkGameStatus(userInput, question)
                    totalScore = status.totalScore
                    if (status.isGameOver) break
                }
            } catch (e: InvalidInputException) {
                println(e.message)
            }
            println("Game Over")
            println("Your score is $totalScore")
            isPlaying = wantToPlayAgain()
        }
    }

    private fun checkGameStatus(
        userAnswer: Int,
        question: IngredientQuestion,
    ): IngredientGameStatus = ingredientGameUseCase.checkGameStatus(userAnswer, question)

    private fun getUserAnswer(chooses: List<Pair<Boolean, String>>): Int {
        val input =
            getUserInput().trim().toIntOrNull() ?: throw InvalidInputException("Invalid input. Please enter a number.")
        val userAnswer = input.dec()

        return if (userAnswer in chooses.indices) {
            userAnswer
        } else {
            throw InvalidInputException("Invalid input. Please enter a number between 1 and ${chooses.size}")
        }
    }

    private fun printQuestion(question: IngredientQuestion) {
        println("What is the correct ingredient for: ${question.mealName}")

        question.chooses.forEachIndexed { index, choose ->
            println("${index.inc()}- $choose")
        }
    }

    private fun wantToPlayAgain(): Boolean { //
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
