package org.seoulsquad.presentation

import logic.model.Meal
import logic.useCase.GetRandomEasyMealsUseCase
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter

class RandomEasyMealsUi(
    private val getRandomEasyMealsUseCase: GetRandomEasyMealsUseCase,
    private val mealPrinter: MealPrinter,
    private val viewer: Viewer
) {
    fun printRandomEasyMeals() {
        val result = getRandomEasyMealsUseCase()

        result
            .onSuccess { randomEasyMealsList ->
                randomEasyMealsList.forEach { meal ->
                    mealPrinter.printFullMeal(meal)
                }
            }.onFailure { exception ->
                viewer.display(exception.message.toString())
            }
    }
}