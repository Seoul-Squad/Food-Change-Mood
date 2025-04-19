package org.seoulsquad.presentation

import logic.useCase.GuessGameUseCase
import logic.useCase.NegativeNumberException

class GuessGameUi(
    private val guessGameUseCase: GuessGameUseCase
) {
    fun startGuessGame() {
        do {
            val meal = guessGameUseCase.generateRandomMeal()
            if (meal == null) {
                println("No meals available!")
                return
            }

            println("Guess the preparation time (in minutes) for: ${meal.name}")

            var attempts = 0
            var isCorrect = false

            while (attempts < 3 && !isCorrect) {
                println("Attempt ${attempts + 1}:")
                print("Enter your guess: ")

                val input = readlnOrNull()
                try {
                    val guess = guessGameUseCase.userGuess(input)

                    isCorrect = guessGameUseCase.guessIsCorrect(guess, meal.preparationTimeInMinutes)

                    when {
                        isCorrect -> println("Correct! You guessed the right time!")
                        guessGameUseCase.guessIsTooHigh(guess, meal.preparationTimeInMinutes) -> println("Too high!")
                        guessGameUseCase.guessIsTooLow(guess, meal.preparationTimeInMinutes) -> println("Too low!")
                    }
                } catch (e: IllegalArgumentException) {
                    println("Invalid input: ${e.message}")
                } catch (e: NegativeNumberException) {
                    println("Invalid input: ${e.message}")
                }

                attempts++
            }

            if (!isCorrect) {
                println("You got it wrong, better luck next time! The correct time was ${meal.preparationTimeInMinutes} minutes.")
            }

            println("\nDo you want to play again? (y/n)")
            val playAgain = readlnOrNull()?.lowercase() == "y"
        } while (playAgain)
    }

}