package logic.useCase

import logic.model.Meal
import org.seoulsquad.logic.repository.MealRepository

class GuessGameUseCase (
    private val mealRepository: MealRepository
) {
    fun generateRandomMeal(): Meal? {
        val meals = mealRepository.getAllMeals().filter { meal ->
            meal.name.isNotBlank()
        }
        return meals.randomOrNull()
    }

    fun guessIsCorrect(guess: Int, actualTime: Int): Boolean {
        return guess == actualTime
    }

    fun guessIsTooHigh(guess: Int, actualTime: Int): Boolean  {
        return guess > actualTime
    }

    fun guessIsTooLow(guess: Int, actualTime: Int): Boolean  {
        return guess < actualTime
    }

    fun userGuess(input: String?): Int {
        val guess = input?.toIntOrNull() ?: throw IllegalArgumentException(" please enter a number.")
        if (guess < 0) throw NegativeNumberException("Input must be greater than 0")
        return guess
    }

}

class NegativeNumberException(message: String) : Exception(message)
