package org.seoulsquad.presentation

import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.useCase.GetRandomIngredientQuestion

class IngredientGameUi(
    private val getRandomIngredientQuestion: GetRandomIngredientQuestion,
) {
    fun startIngredientGame(maxRounds: Int = 15) {
        var totalScore = 0
        var roundNumber = 1
        var isGameOver = false

        while (roundNumber <= maxRounds && !isGameOver) {
            getRandomIngredientQuestion()
                .onSuccess { question ->
                    printQuestion(question)
                    getUserAnswer().also {
                        checkUserAnswer(
                            it,
                            question,
                            {
                                totalScore += SCORE_PER_ROUND
                                roundNumber++
                            },
                            { isGameOver = true },
                        )
                    }
                }.onFailure { error ->
                    println(error.message)
                    isGameOver = true
                }
        }
        println("your score: $totalScore")
    }

    private fun checkUserAnswer(
        userAnswer: String,
        question: IngredientQuestion,
        increaseScoreAndIncrementRoundNumber: () -> Unit,
        gameOver: () -> Unit,
    ) {
        if (userAnswer.isNotBlank()) {
            if (question.chooses[userAnswer.toInt().dec()].first) {
                increaseScoreAndIncrementRoundNumber.invoke()
            } else {
                println("Wrong answer, Game Over")
                gameOver()
            }
        } else {
            println("Invalid input")
        }
    }

    private fun printQuestion(question: IngredientQuestion) {
        val meal = question.meal
        val chooses = question.chooses

        println("What is the right ingredient for: ${meal.name}")

        chooses.forEachIndexed { index, choose ->
            println("${index.inc()}- ${choose.second}")
        }
    }

    private fun getUserAnswer() = readlnOrNull() ?: ""

    companion object {
        const val SCORE_PER_ROUND = 1000
    }
}
