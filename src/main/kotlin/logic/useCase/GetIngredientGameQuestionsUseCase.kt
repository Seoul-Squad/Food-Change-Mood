package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.NoIngredientFoundException
import logic.utils.NotEnoughMealsFoundException
import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.repository.MealRepository

class GetIngredientGameQuestionsUseCase(
    private val mealRepository: MealRepository,
) {
    operator fun invoke(): List<IngredientQuestion> {
        val questions = mutableSetOf<IngredientQuestion>()
        while (questions.size < NUMBER_OF_GAME_ROUNDS) {
            questions.add(generateIngredientGameQuestion())
        }
        return questions.toList()
    }

    private fun generateIngredientGameQuestion(): IngredientQuestion {
        val allMeals = mealRepository.getAllMeals()
        if (allMeals.size < NUMBER_OF_INGREDIENT_QUESTION) {
            throw NotEnoughMealsFoundException()
        }
        val randomMeal = allMeals.random()
        val correctAnswer =
            randomMeal.ingredients.randomOrNull()
                ?: throw NoIngredientFoundException()
        val otherMeals = getOtherMeals(allMeals, randomMeal).take(NUMBER_OF_INGREDIENT_QUESTION)
        val wrongAnswers = getRandomWrongOptions(randomMeal, otherMeals)
        if (wrongAnswers.size < NUMBER_OF_INGREDIENT_QUESTION) {
            throw NotEnoughMealsFoundException()
        }
        val options = getQuestionOptions(correctAnswer, wrongAnswers)
        return IngredientQuestion(randomMeal.name, options.shuffled())
    }

    private fun getOtherMeals(
        allMeals: List<Meal>,
        randomMeal: Meal,
    ): List<Meal> =
        allMeals
            .filter { it.id != randomMeal.id }
            .shuffled()

    private fun getQuestionOptions(
        correctAnswer: String,
        wrongAnswers: List<String>,
    ): List<Pair<Boolean, String>> = listOf(true to correctAnswer) + wrongAnswers.map { false to it }

    private fun getRandomWrongOptions(
        meal: Meal,
        allMeals: List<Meal>,
    ): List<String> {
        val mealIngredients = meal.ingredients.map { it.lowercase() }.toSet()

        val candidates =
            allMeals
                .flatMap { it.ingredients }
                .filterNot { ingredient ->
                    mealIngredients.any { it.contains(ingredient, ignoreCase = true) }
                }

        return candidates.take(NUMBER_OF_INGREDIENT_QUESTION)
    }

    companion object {
        const val NUMBER_OF_INGREDIENT_QUESTION = 2
        const val NUMBER_OF_GAME_ROUNDS = 15
    }
}
