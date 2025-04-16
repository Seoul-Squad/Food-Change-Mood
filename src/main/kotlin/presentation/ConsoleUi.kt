package org.seoulsquad.presentation

import org.seoulsquad.logic.use_case.GetRandomPotatoMealsUseCase
import org.seoulsquad.model.Meal
import org.seoulsquad.presentation.utils.ConsoleColors
import org.seoulsquad.presentation.utils.ConsoleStyle

class ConsoleUi(
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase,
) {
    private fun startShowRandomPotatoMeals() {
        println("${ConsoleStyle.BOLD}${ConsoleColors.CYAN}This is very yummy, try this potato meals and Ed3yly.${ConsoleStyle.RESET}")
        getRandomPotatoMealsUseCase()
            .onSuccess { mealsWithPotato ->
                printPotatoMealsResult(mealsWithPotato)
            }.onFailure { exception ->
                println(exception.message)
            }
    }

    private fun printPotatoMealsResult(mealsWithPotato: List<Meal>) {
        if (mealsWithPotato.size < 10) {
            println("Unfortunately we don't have 10 meals right now.\uD83E\uDD7A")
            Thread.sleep(500)
            println("But cheers up he have: ${mealsWithPotato.size}\uD83D\uDE03")
            Thread.sleep(500)
            println("So here is ${mealsWithPotato.size} potato meals instead\uD83D\uDE09\n\n")
            Thread.sleep(500)
        }
        mealsWithPotato.forEach { printFullMeal(it) }
        println("Bon-appetit")
    }

    private fun printFullMeal(meal: Meal) {
        with(meal) {
            println("Meal: $name (ID: $id)")
            println("Time to Prepare: $minutes minutes")
            meal.description.takeIf { !it.isNullOrBlank() }.run { println("$this") }
            println("Ingredients ($numberOfIngredients):")
            ingredients.forEachIndexed { index, ingredient ->
                println("  ${index + 1}. $ingredient")
            }
            println("Steps ($numberOfSteps):")
            steps.forEachIndexed { index, step ->
                println("  Step ${index + 1}: $step")
            }
            println("Nutrition:")
            println("  - Calories: ${nutrition.calories} kcal")
            println("  - Total Fat: ${nutrition.totalFat} g")
            println("  - Saturated Fat: ${nutrition.saturatedFat} g")
            println("  - Sugar: ${nutrition.sugar} g")
            println("  - Sodium: ${nutrition.sodium} mg")
            println("  - Protein: ${nutrition.protein} g")
            println("  - Carbohydrates: ${nutrition.carbohydrates} g")
        }
        println("\n===========================================================\n")
    }
}
