package org.seoulsquad.logic.useCase

import org.seoulsquad.logic.model.IngredientGameStatus
import org.seoulsquad.logic.model.IngredientQuestion

class GetIngredientGameStatusUseCase {
    private var totalScore = 0
    private var isGameOver = false

    operator fun invoke(
        userAnswer: Int,
        question: IngredientQuestion,
    ): IngredientGameStatus {
        checkGameStatus()
        isCorrectAnswer(
            userAnswer,
            question,
        ).also {
            if (it) increaseScore()
            isGameOver = !it
        }
        return IngredientGameStatus(totalScore, isGameOver)
    }

    private fun checkGameStatus() {
        if (isGameOver) resetGameStatus()
    }

    private fun increaseScore() {
        totalScore += SCORE_PER_ROUND
    }

    private fun isCorrectAnswer(
        userAnswer: Int,
        question: IngredientQuestion,
    ): Boolean = question.chooses[userAnswer].first

    private fun resetGameStatus() {
        totalScore = 0
    }

    companion object {
        const val SCORE_PER_ROUND = 1000
    }
}
