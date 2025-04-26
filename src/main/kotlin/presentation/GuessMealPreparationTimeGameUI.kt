package org.seoulsquad.presentation

import logic.model.GuessResult
import logic.useCase.GuessMealPreparationTimeGameUseCase
import logic.model.Meal
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer


class GuessMealPreparationTimeGameUI(
    private val guessGameUseCase: GuessMealPreparationTimeGameUseCase,
    private val viewer: Viewer,
    private val reader: Reader
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
                initializeNewGameRound()
            } else {
                manageGuessAttempt(meal)
            }
        }
    }


    private fun doesUserWantToPlayAgain(): Boolean {
        val playAgainInput = askUserToPlayAgain()?.trim()?.lowercase()
        when (playAgainInput) {
            "y" -> {guessGameUseCase(GuessResult.PLAY_AGAIN.name)
                return true}
            "n" -> {guessGameUseCase(GuessResult.EXIT.name)
                return false}

            else -> {
                displayExitMessage()
                return false
            }
        }
    }

    private fun initializeNewGameRound(): Boolean {
        val initResult = guessGameUseCase(null)
        return initResult.fold(
            onSuccess = { outcome ->
                if (outcome == GuessResult.GAME_STARTED) {
                    val newMeal = guessGameUseCase.getGameState().currentMeal
                    if (newMeal == null) {
                        viewer.display("No meal found.")
                        return false
                    } else {
                        manageGuessAttempt(newMeal)
                        return true
                    }
                } else {
                    viewer.display("Unexpected game start outcome")
                    return false
                }
            },
            onFailure = { e ->
                viewer.display("Error starting game: ${e.message ?: "Unknown error"}")
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
                viewer.display("Error processing guess: ${e.message ?: "An error occurred"}")
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
        viewer.display("\nDo you want to play again? (y/n)")
        return reader.readString()
    }

    private fun askUserToEnterGuess(meal: Meal, currentAttempt: Int, maxAttempts: Int): String? {
        viewer.display("\nGuess the preparation time (in minutes) for: ${meal.name}")
        viewer.display("Attempt ${currentAttempt + 1} of $maxAttempts:")
        viewer.display("Enter your guess: ")
        return reader.readString()
    }

    private fun displayCorrectGuess() {
        viewer.display("Correct! You guessed the right time!")
    }

    private fun displayIncorrectGuess(isTooHigh: Boolean) {
        viewer.display(if (isTooHigh) "Too high! Try again." else "Too low! Try again.")
    }

    private fun displayGameOver(isTooHigh: Boolean, maxAttempts: Int, correctTime: Int) {
        viewer.display(if (isTooHigh) "Too high!" else "Too low!")
        viewer.display("Game Over! You've used all $maxAttempts attempts. The correct time was $correctTime minutes.")
    }

    private fun displayExitMessage() {
        viewer.display("Thanks for playing!")
    }

}