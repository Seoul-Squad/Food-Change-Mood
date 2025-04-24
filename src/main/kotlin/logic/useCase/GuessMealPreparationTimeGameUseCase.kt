package logic.useCase

import logic.model.GuessResult
import logic.model.Meal
import logic.utils.InvalidNumberException
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.model.GameState
import org.seoulsquad.logic.repository.MealRepository

class GuessMealPreparationTimeGameUseCase(
    private val mealRepository: MealRepository
) {
    private var currentMeal: Meal? = null
    private var currentAttempt: Int = 0
    private var maxAttempts: Int = 3
    private var startNewRound: Boolean = false

    operator fun invoke(guess: String?): Result<GuessResult> {
        if (guess == GuessResult.PLAY_AGAIN.name) {
            resetGame()
            return Result.success(GuessResult.PLAY_AGAIN)
        }
        if (currentMeal == null || startNewRound) {
            return initializeGame()
        }
        return evaluateUserGuess(guess)
    }

    private fun resetGame() {
        currentMeal = null
        currentAttempt = 0
        startNewRound = false
    }

    fun getGameState(): GameState = GameState(
        currentMeal = currentMeal,
        currentAttempt = currentAttempt,
        maxAttempts = maxAttempts,
        shouldStartNewRound = startNewRound
    )
    private fun initializeGame(): Result<GuessResult> {
        currentMeal = generateRandomMeal().getOrElse { return Result.failure(it) }
        currentAttempt = 0
        startNewRound = false
        return Result.success(GuessResult.GAME_STARTED)
    }

    private fun evaluateUserGuess(guess: String?): Result<GuessResult> {
        return try {
            val validUserInput = validateGuess(guess)
            currentAttempt++
            if (currentAttempt > maxAttempts) {
                startNewRound = true
                return Result.success(GuessResult.TOO_LOW)
            }
            Result.success(determineGuessResult(validUserInput))
        } catch (e: InvalidNumberException) {
            Result.failure(e)
        }

    }
    private fun validateGuess(guess: String?): Int {
        val number = guess?.toIntOrNull() ?: throw InvalidNumberException()
        if (number <= 0) throw InvalidNumberException()
        return number

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
            ?: Result.failure(NoMealsFoundException())
    }
}