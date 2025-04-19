package logic.useCase

import logic.model.GuessResult
import logic.model.Meal
import logic.utils.InvalidGuessException
import logic.utils.NegativeGuessException
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GuessMealPreparationTimeGameUseCase(
    private val mealRepository: MealRepository
) {
    private var currentMeal: Meal? = null
    private var currentAttempt: Int = 0
    private var maxAttempts: Int = 3
    private var startNewRound: Boolean = false

    operator fun invoke(guess: String?): Result<GuessResult> {
        if (currentMeal == null || startNewRound) {
            return initializeGame()
        }
        return evaluateUserGuess(guess)
    }

    fun doesUserWantToPlayAgain(input: String?): Result<Boolean> {
        val normalizedInput = input?.trim()?.lowercase()
        val playAgain = normalizedInput == "y" || normalizedInput == "yes"
        if (playAgain) {
            currentMeal = null
            currentAttempt = 0
            startNewRound = false
        } else {
            currentMeal = null
            currentAttempt = 0
            startNewRound = false
        }
        return Result.success(playAgain)
    }

    fun getCurrentMeal(): Meal? = currentMeal
    fun getCurrentAttempt(): Int = currentAttempt
    fun getMaxAttempts(): Int = maxAttempts
    fun shouldStartNewRound(): Boolean = startNewRound

    private fun initializeGame(): Result<GuessResult> {
        currentMeal = generateRandomMeal().getOrElse { return Result.failure(it) }
        currentAttempt = 0
        startNewRound = false
        return Result.success(GuessResult.GAME_STARTED)
    }

    private fun evaluateUserGuess(guess: String?): Result<GuessResult> {
        return try {
            val guessValue = validateGuess(guess)
            currentAttempt++
            if (currentAttempt > maxAttempts) {
                startNewRound = true
                return Result.success(GuessResult.TOO_LOW)
            }
            Result.success(determineGuessResult(guessValue))
        } catch (e: InvalidGuessException) {
            Result.failure(e)
        } catch (e: NegativeGuessException) {
            Result.failure(e)
        }

    }
    private fun validateGuess(guess: String?): Int {
        val number = guess?.toIntOrNull()
            ?: throw InvalidGuessException("Please enter a valid number")

        return if (number >= 0) {
            number
        } else {
            throw NegativeGuessException("Time must be positive")
        }
    }

    private fun determineGuessResult(guess: Int): GuessResult {
        val result = when {
            guess == currentMeal?.preparationTimeInMinutes -> GuessResult.CORRECT
            guess > (currentMeal?.preparationTimeInMinutes ?: 0) -> GuessResult.TOO_HIGH
            else -> GuessResult.TOO_LOW
        }
        if (result == GuessResult.CORRECT || currentAttempt >= maxAttempts) {
            startNewRound = true
        }
        return result
    }

    private fun generateRandomMeal(): Result<Meal> {
        val meals = mealRepository.getAllMeals().filter { it.name.isNotBlank() }
        return meals.randomOrNull()?.let { Result.success(it) }
            ?: Result.failure(NoMealsFoundException("No meals available!"))
    }
}