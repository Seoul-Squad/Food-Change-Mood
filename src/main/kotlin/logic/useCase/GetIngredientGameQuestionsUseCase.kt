package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.NoIngredientFoundException
import logic.utils.NotEnoughMealsFoundException
import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.shuffledByIndices

class GetIngredientGameQuestionsUseCase(
    private val mealRepository: MealRepository,
) {
    operator fun invoke(
        questionsLimit: Int = NUMBER_OF_GAME_QUESTIONS,
        optionsLimit: Int = NUMBER_OF_QUESTION_OPTIONS,
    ): Result<List<IngredientQuestion>> =
        try {
            val questionsList =
                generateSequence {
                    generateIngredientGameQuestion(optionsLimit)
                }.distinct()
                    .take(questionsLimit)
                    .toList()

            Result.success(questionsList)
        } catch (e: Exception) {
            Result.failure(e)
        }

    private fun generateIngredientGameQuestion(optionsLimit: Int): IngredientQuestion {
        val allMeals =
            getAllMeals(optionsLimit)
        val meal = allMeals.random()
        val correctAnswer = getCorrectAnswer(meal)
        val wrongAnswers =
            getRandomWrongOptions(
                meal,
                getOtherMeals(allMeals, meal, optionsLimit),
                optionsLimit,
            )
        val options = getQuestionOptions(correctAnswer, wrongAnswers)
        return IngredientQuestion(meal.name, options.shuffled())
    }

    private fun getCorrectAnswer(randomMeal: Meal): String =
        randomMeal
            .ingredients
            .randomOrNull()
            ?: throw NoIngredientFoundException()

    private fun getAllMeals(optionsLimit: Int): List<Meal> =
        mealRepository
            .getAllMeals()
            .takeIf { it.size >= optionsLimit }
            ?: throw NotEnoughMealsFoundException()

    private fun getOtherMeals(
        allMeals: List<Meal>,
        meal: Meal,
        optionsLimit: Int,
    ): List<Meal> =
        allMeals
            .filter { it.id != meal.id }
            .shuffledByIndices(optionsLimit)

    private fun getRandomWrongOptions(
        meal: Meal,
        allMeals: List<Meal>,
        optionsLimit: Int,
    ): List<String> {
        val mealIngredients = meal.ingredients.map { it.lowercase() }.toSet()

        val candidates =
            allMeals
                .flatMap { it.ingredients }
                .filterNot { ingredient ->
                    mealIngredients.any { it.contains(ingredient, ignoreCase = true) }
                }

        return candidates
            .takeIf {
                it.size >= optionsLimit.dec()
            }?.take(optionsLimit) ?: throw NotEnoughMealsFoundException()
    }

    private fun getQuestionOptions(
        correctAnswer: String,
        wrongAnswers: List<String>,
    ): List<Pair<Boolean, String>> = listOf(true to correctAnswer) + wrongAnswers.map { false to it }

    companion object {
        const val NUMBER_OF_QUESTION_OPTIONS = 3
        const val NUMBER_OF_GAME_QUESTIONS = 15
    }
}
