package org.seoulsquad.presentation

import logic.model.GuessResult
import logic.model.Meal
import logic.useCase.GuessMealPreparationTimeGameUseCase

class GuessMealPreparationTimeGameUI(
    private val guessGameUseCase: GuessMealPreparationTimeGameUseCase,
) {
    fun startGuessGame() {
        manageGameFlow()
    }

    private fun manageGameFlow() {
        var shouldContinueGame = true

        while (shouldContinueGame) {
            if (guessGameUseCase.shouldStartNewRound()) {
                shouldContinueGame = doesUserWantToPlayAgain()
            }

            val meal = guessGameUseCase.getCurrentMeal()
            if (meal == null) {
                shouldContinueGame = initializeNewGameRound()
                if (!shouldContinueGame) break
            } else {
                manageGuessAttempt(meal)
            }
        }
    }

    private fun doesUserWantToPlayAgain(): Boolean {
        val playAgainInput = askUserToPlayAgain()
        val playAgainResult = guessGameUseCase.doesUserWantToPlayAgain(playAgainInput)

        return playAgainResult.fold(
            onSuccess = { shouldContinue ->
                if (!shouldContinue) {
                    displayExitMessage()
                    return false
                }
                guessGameUseCase(null)
                true
            },
            onFailure = { e ->
                displayError("Error processing play again: ${e.message}")
                false
            },
        )
    }

    private fun initializeNewGameRound(): Boolean {
        val initResult = guessGameUseCase(null)
        return initResult.fold(
            onSuccess = { outcome ->
                if (outcome == GuessResult.GAME_STARTED) {
                    val newMeal = guessGameUseCase.getCurrentMeal()
                    if (newMeal == null) {
                        displayError("No meals available!")
                        false
                    } else {
                        manageGuessAttempt(newMeal)
                        true
                    }
                } else {
                    displayError("Unexpected game start outcome")
                    false
                }
            },
            onFailure = { e ->
                displayError("Error starting game: ${e.message ?: "No meals found"}")
                false
            },
        )
    }

    private fun manageGuessAttempt(meal: Meal) {
        val guessInput =
            askUserToEnterGuess(
                meal = meal,
                currentAttempt = guessGameUseCase.getCurrentAttempt(),
                maxAttempts = guessGameUseCase.getMaxAttempts(),
            )
        validateAndProcessGuess(guessInput)
    }

    private fun validateAndProcessGuess(input: String?) {
        val guessResult = guessGameUseCase(input)
        guessResult.fold(
            onSuccess = ::handleGuessResult,
            onFailure = { e ->
                displayError("Error processing guess: ${e.message ?: "An error occurred"}")
            },
        )
    }

    private fun handleGuessResult(result: GuessResult) {
        when (result) {
            GuessResult.CORRECT -> displayCorrectGuess()
            GuessResult.TOO_HIGH -> handleIncorrectGuess(isTooHigh = true)
            GuessResult.TOO_LOW -> handleIncorrectGuess(isTooHigh = false)
            GuessResult.INVALID -> displayInvalidInput()
            GuessResult.NEGATIVE -> displayNegativeInput()
            GuessResult.GAME_STARTED -> displayNewGameStartMessage()
        }
    }

    private fun handleIncorrectGuess(isTooHigh: Boolean) {
        if (guessGameUseCase.shouldStartNewRound()) {
            displayGameOver(
                isTooHigh = isTooHigh,
                maxAttempts = guessGameUseCase.getMaxAttempts(),
                correctTime = guessGameUseCase.getCurrentMeal()!!.preparationTimeInMinutes,
            )
        } else {
            displayIncorrectGuess(isTooHigh = isTooHigh)
        }
    }

    private fun askUserToPlayAgain(): String? {
        println("\nDo you want to play again? (y/n)")
        return readlnOrNull()
    }

    private fun askUserToEnterGuess(
        meal: Meal,
        currentAttempt: Int,
        maxAttempts: Int,
    ): String? {
        println("\nGuess the preparation time (in minutes) for: ${meal.name}")
        println("Attempt ${currentAttempt + 1} of $maxAttempts:") // Show attempt as 1-based
        print("Enter your guess: ")
        return readlnOrNull()
    }

    private fun displayCorrectGuess() {
        println("Correct! You guessed the right time!")
    }

    private fun displayIncorrectGuess(isTooHigh: Boolean) {
        println(if (isTooHigh) "Too high! Try again." else "Too low! Try again.")
    }

    private fun displayGameOver(
        isTooHigh: Boolean,
        maxAttempts: Int,
        correctTime: Int,
    ) {
        println(if (isTooHigh) "Too high!" else "Too low!")
        println("Game Over! You've used all $maxAttempts attempts. The correct time was $correctTime minutes.")
    }

    private fun displayInvalidInput() {
        println("Invalid input: Please enter a number.")
    }

    private fun displayNegativeInput() {
        println("Invalid input: Input must be greater than 0")
    }

    private fun displayError(message: String) {
        println("\n$message")
    }

    private fun displayExitMessage() {
        println("Thanks for playing!")
    }

    private fun displayNewGameStartMessage() {
        println("New game started! Guess the preparation time for the next meal.")
    }
}
