package org.seoulsquad.presentation

import logic.useCase.GetRandomEasyMealsUseCase
import org.seoulsquad.presentation.utils.SharedUi

class RandomEasyMealsUi(
    private val getRandomEasyMealsUseCase: GetRandomEasyMealsUseCase
) {
    fun printRandomEasyMeals() {
        val result = getRandomEasyMealsUseCase()

        result
            .onSuccess { randomEasyMealsList ->
                randomEasyMealsList.forEach { meal ->
                    SharedUi().printFullMeal(meal)
                }
            }.onFailure { exception ->
                println(exception.message)
            }
    }
}