package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.NoIngredientFoundException
import logic.utils.NotEnoughMealsFoundException
import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.repository.MealRepository

class GetRandomIngredientQuestion(
    private val mealRepository: MealRepository,
) {
    operator fun invoke(numberOfIngredientQuestion: Int = 2): Result<IngredientQuestion> {
        val allMeals = mealRepository.getAllMeals()
        if (allMeals.size < numberOfIngredientQuestion) {
            return Result.failure(NotEnoughMealsFoundException())
        }

        val randomMeal = allMeals.random()
        val correctAnswer =
            randomMeal.ingredients.randomOrNull()
                ?: return Result.failure(NoIngredientFoundException())

        val otherMeals =
            allMeals
                .filter { it.id != randomMeal.id }
                .shuffled()
                .take(numberOfIngredientQuestion)

        val wrongAnswers = getRandomWrongOptions(randomMeal, otherMeals)

        if (wrongAnswers.size < numberOfIngredientQuestion) {
            return Result.failure(NoIngredientFoundException())
        }

        val options =
            listOf(true to correctAnswer) + wrongAnswers.map { false to it }

        return Result.success(IngredientQuestion(randomMeal, options.shuffled()))
    }

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

        return candidates.take(allMeals.size)
    }
}
