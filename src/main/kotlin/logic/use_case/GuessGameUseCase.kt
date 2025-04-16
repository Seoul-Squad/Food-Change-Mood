package org.seoulsquad.logic.use_case

import org.seoulsquad.logic.repository.MealRepository

class GuessGameUseCase (
    private val mealRepository: MealRepository
) {
    fun generateRandomMeal() {
        TODO("implement code for generating random meal")
    }

    fun guessIsCorrect() {
        TODO("implement code showing that returns that the guess is correct")
    }

    fun guessIsTooHigh() {
        TODO("implement code that checks if the guess is too high")
    }

    fun guessIsTooLow() {
        TODO("implement code that checks if the guess is too low")
    }

    fun userGuess() {
        TODO("implement code to take input from user")
    }

    fun gameResult() {
        TODO("implement code to show game result")
    }
}
