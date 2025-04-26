package org.seoulsquad.logic.useCase

import logic.utils.InvalidNumberException
import org.seoulsquad.logic.model.IngredientGameStatus
import org.seoulsquad.logic.model.IngredientQuestion

class GetIngredientGameStatusUseCase {
    private var totalScore = 0
    private var isGameOver = false

    operator fun invoke(
        userAnswer: Int?,
        question: IngredientQuestion,
    ): Result<IngredientGameStatus> =
        try {
            checkGameStatus()
            isCorrectAnswer(
                checkUserInput(userAnswer, question.chooses),
                question,
            ).also {
                if (it) increaseScore()
                isGameOver = !it
            }
            Result.success(IngredientGameStatus(totalScore, isGameOver))
        } catch (e: Exception) {
            Result.failure(e)
        }

    private fun checkUserInput(
        userAnswer: Int?,
        chooses: List<Pair<Boolean, String>>,
    ): Int =
        userAnswer
            .takeIf { it != null }
            ?.dec()
            ?.takeIf { it in chooses.indices }
            ?: throw InvalidNumberException()

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
