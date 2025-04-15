package org.seoulsquad.presentation

import data.model.Meal
import org.seoulsquad.logic.use_case.GetTenRandomEasyMealsUseCase

class ConsoleUi(
    private val getTenRandomEasyMealsUseCase: GetTenRandomEasyMealsUseCase
) {

    private fun showTenRandomEasyMeals() {
        getTenRandomEasyMealsUseCase().forEach { meal->
            printMeal(meal)
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

            println("Steps ($numberOfSteps):")
            steps.forEachIndexed { index, step ->
                println("${index+1}- $step")
            }
        }
    }
}