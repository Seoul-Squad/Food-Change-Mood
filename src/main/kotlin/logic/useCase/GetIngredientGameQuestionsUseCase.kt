package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.InvalidNumberException
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
                    generateIngredientGameQuestion(questionsLimit, optionsLimit)
                }.distinctBy { it.id }
                    .take(questionsLimit)
                    .toList()

            Result.success(questionsList)
        } catch (e: Exception) {
            Result.failure(e)
        }

    private fun generateIngredientGameQuestion(
        questionsLimit: Int,
        optionsLimit: Int,
    ): IngredientQuestion {
        if (questionsLimit < 3 || optionsLimit < 3) throw InvalidNumberException()
        val allMeals =
            getAllMeals(questionsLimit)
        val meal = allMeals.random()
        val correctAnswer = getCorrectAnswer(meal)
        val wrongAnswers =
            getRandomWrongOptions(
                meal,
                getOtherMeals(allMeals, meal, questionsLimit),
                optionsLimit,
            )
        val options = getQuestionOptions(correctAnswer, wrongAnswers)
        return IngredientQuestion(meal.id, meal.name, options.shuffled())
    }

    private fun getCorrectAnswer(randomMeal: Meal): String =
        randomMeal
            .ingredients
            .randomOrNull()
            ?: throw NoIngredientFoundException()

    private fun getAllMeals(questionsLimit: Int): List<Meal> =
        mealRepository
            .getAllMeals()
            .takeIf { it.size >= questionsLimit }
            ?: throw NotEnoughMealsFoundException()

    private fun getOtherMeals(
        allMeals: List<Meal>,
        meal: Meal,
        questionsLimit: Int,
    ): List<Meal> =
        allMeals
            .filter { it.id != meal.id }
            .shuffledByIndices(questionsLimit)

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

        return candidates.take(optionsLimit.dec())
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
