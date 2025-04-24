package org.seoulsquad.presentation

import logic.model.GuessResult
import logic.useCase.GuessMealPreparationTimeGameUseCase
import logic.model.Meal


class GuessMealPreparationTimeGameUI(
    private val guessGameUseCase: GuessMealPreparationTimeGameUseCase
) {
    fun startGuessGame() {
        manageGameFlow()
    }

    private fun manageGameFlow() {
        while (true) {
            if (guessGameUseCase.getGameState().shouldStartNewRound) {
                if (!doesUserWantToPlayAgain()) break
            }

            val meal = guessGameUseCase.getGameState().currentMeal
            if (meal == null) {
                shouldInitializeNewGameRound()
            } else {
                manageGuessAttempt(meal)
            }
        }
    }

    private fun doesUserWantToPlayAgain(): Boolean {
        val playAgainInput = askUserToPlayAgain()?.trim()?.lowercase()
        val playAgain = playAgainInput == "y" || playAgainInput == "yes"
        if (playAgain) {
            guessGameUseCase(GuessResult.PLAY_AGAIN.name)
            return true
        } else {
            displayExitMessage()
            return false
        }
    }

    private fun shouldInitializeNewGameRound(): Boolean {
        val initResult = guessGameUseCase(null)
        return initResult.fold(
            onSuccess = { outcome ->
                if (outcome == GuessResult.GAME_STARTED) {
                    val newMeal = guessGameUseCase.getGameState().currentMeal
                    if (newMeal == null) {
                        displayError("No meal found.")
                        return false
                    } else {
                        manageGuessAttempt(newMeal)
                        return true
                    }
                } else {
                    displayError("Unexpected game start outcome")
                    return false
                }
            },
            onFailure = { e ->
                displayError("Error starting game: ${e.message ?: "Unknown error"}")
                return false
            }
        )
    }

    private fun manageGuessAttempt(meal: Meal) {
        val guessInput = askUserToEnterGuess(
            meal = meal,
            currentAttempt = guessGameUseCase.getGameState().currentAttempt,
            maxAttempts = guessGameUseCase.getGameState().maxAttempts
        )
        validateAndProcessGuess(guessInput)
    }

    private fun validateAndProcessGuess(input: String?) {
        val guessResult = guessGameUseCase(input)
        guessResult.fold(
            onSuccess = ::handleGuessResult,
            onFailure = { e ->
                displayError("Error processing guess: ${e.message ?: "An error occurred"}")
            }
        )
    }

    private fun handleGuessResult(result: GuessResult) {
        when (result) {
            GuessResult.CORRECT -> displayCorrectGuess()
            GuessResult.TOO_HIGH -> handleIncorrectGuess(isTooHigh = true)
            GuessResult.TOO_LOW -> handleIncorrectGuess(isTooHigh = false)
            else -> {}
        }
    }

    private fun handleIncorrectGuess(isTooHigh: Boolean) {
        if (guessGameUseCase.getGameState().shouldStartNewRound) {
            displayGameOver(
                isTooHigh = isTooHigh,
                maxAttempts = guessGameUseCase.getGameState().maxAttempts,
                correctTime = guessGameUseCase.getGameState().currentMeal!!.preparationTimeInMinutes
            )
        } else {
            displayIncorrectGuess(isTooHigh = isTooHigh)
        }
    }

    private fun askUserToPlayAgain(): String? {
        println("\nDo you want to play again? (y/n)")
        return readlnOrNull()
    }

    private fun askUserToEnterGuess(meal: Meal, currentAttempt: Int, maxAttempts: Int): String? {
        println("\nGuess the preparation time (in minutes) for: ${meal.name}")
        println("Attempt ${currentAttempt + 1} of $maxAttempts:")
        print("Enter your guess: ")
        return readlnOrNull()
    }

    private fun displayCorrectGuess() {
        println("Correct! You guessed the right time!")
    }

    private fun displayIncorrectGuess(isTooHigh: Boolean) {
        println(if (isTooHigh) "Too high! Try again." else "Too low! Try again.")
    }

    private fun displayGameOver(isTooHigh: Boolean, maxAttempts: Int, correctTime: Int) {
        println(if (isTooHigh) "Too high!" else "Too low!")
        println("Game Over! You've used all $maxAttempts attempts. The correct time was $correctTime minutes.")
    }


    private fun displayError(message: String) {
        println("\n$message")
    }

    private fun displayExitMessage() {
        println("Thanks for playing!")
    }

}
