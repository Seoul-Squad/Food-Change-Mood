package org.seoulsquad.presentation

import logic.useCase.GetRandomEasyMealsUseCase
import org.seoulsquad.presentation.utils.MealPrinter

class RandomEasyMealsUi(
    private val getRandomEasyMealsUseCase: GetRandomEasyMealsUseCase,
    private val mealPrinter: MealPrinter
) {
    fun printRandomEasyMeals() {
        val result = getRandomEasyMealsUseCase()

        result
            .onSuccess { randomEasyMealsList ->
                randomEasyMealsList.forEach { meal ->
                    mealPrinter.printFullMeal(meal)
                }
            }.onFailure { exception ->
                println(exception.message)
            }
    }
}