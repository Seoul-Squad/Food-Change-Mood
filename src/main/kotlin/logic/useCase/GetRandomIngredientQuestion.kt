package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.NoEnoughMealsFoundException
import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.repository.MealRepository

class GetRandomIngredientQuestion(
    private val mealRepository: MealRepository,
) {
    operator fun invoke(): Result<IngredientQuestion> {
        val allMeals = mealRepository.getAllMeals()
        if (allMeals.size < 2) return Result.failure(NoEnoughMealsFoundException("Not enough meals to generate a question."))

        val randomMeal = allMeals.random()
        val correctAnswer =
            randomMeal.ingredients.randomOrNull()
                ?: return Result.failure(Exception("Meal has no ingredients."))

        val otherMeals = allMeals.filter { it.id != randomMeal.id }
        val wrongAnswers = getRandomWrongAnswers(randomMeal, otherMeals)

        if (wrongAnswers.size < 2) {
            return Result.failure(Exception("Not enough wrong answers."))
        }

        val answers =
            listOf(
                true to correctAnswer,
                false to wrongAnswers[0],
                false to wrongAnswers[1],
            ).shuffled()

        return Result.success(IngredientQuestion(randomMeal, answers))
    }

    private fun getRandomWrongAnswers(
        meal: Meal,
        allMeal: List<Meal>,
    ): List<String> {
        val mealIngredients = meal.ingredients
        val randomIngredient =
            allMeal
                .shuffled()
                .take(2)
                .flatMap { it.ingredients }
                .take(2)

        return if (mealIngredients.any {
                it.contains(
                    randomIngredient[0],
                    ignoreCase = true,
                ) &&
                    it.contains(
                        randomIngredient[1],
                        ignoreCase = true,
                    )
            }
        ) {
            getRandomWrongAnswers(
                meal,
                allMeal,
            )
        } else {
            randomIngredient
        }
    }
}
