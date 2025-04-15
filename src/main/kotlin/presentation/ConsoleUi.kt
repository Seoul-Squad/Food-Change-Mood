package org.seoulsquad.presentation

import data.model.Meal
import org.seoulsquad.logic.use_case.GetRandomEasyMealsUseCase

class ConsoleUi(
    private val getTenRandomEasyMealsUseCase: GetRandomEasyMealsUseCase
) {



    private fun printRandomEasyMeals() {
        val result = getTenRandomEasyMealsUseCase()

        result.onSuccess { randomEasyMealsList ->
            randomEasyMealsList.forEach { meal ->
                printMeal(meal)
            }
        }.onFailure { exception->
            println(exception.message)
        }

    }


    private fun printMeal(meal: Meal) {
        with(meal){
            println("Meal: $name")
            println("Prepare time: $minutes minutes")

            println("Ingredients:")
            ingredients.forEachIndexed { index, ingredient->
                println("${index+1}- $ingredient")
            }

            println("Steps:")
            steps.forEachIndexed { index, step ->
                println("${index+1}- $step")
            }
        }
    }

}