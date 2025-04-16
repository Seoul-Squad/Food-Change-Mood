package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.repository.MealRepository
import org.seoulsquad.logic.model.IngredientQuestion

class IngredientGameUseCase(
    private val mealRepository: MealRepository,
) {
    operator fun invoke(): Result<IngredientQuestion> {
        val randomMeal = mealRepository.getAllMeals().random()
        val allMeals = mealRepository.getAllMeals().filter { it.id != randomMeal.id }.shuffled()
        val correctAnswer = randomMeal.ingredients.random()
        val wrongAnswer = getRandomWrongAnswers(randomMeal, allMeals)
        val answers =
            listOf(
                true to correctAnswer,
                false to wrongAnswer[0],
                false to wrongAnswer[1],
            ).shuffled()

        println(answers.size)

        return Result.success(IngredientQuestion(randomMeal, answers))
    }

    private fun getRandomWrongAnswers(
        meal: Meal,
        allMeal: List<Meal>,
    ): List<String> {
        val mealIngredients = meal.ingredients
        val randomIngredient = allMeal.take(2).flatMap { it.ingredients }.take(2)

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
