package org.seoulsquad.presentation

import org.seoulsquad.logic.use_case.GetTenRandomPotatoMealsUseCase
import org.seoulsquad.model.Meal
import org.seoulsquad.presentation.utils.ConsoleColors
import org.seoulsquad.presentation.utils.ConsoleStyle

class ConsoleUi(
    private val getTenRandomPotatoMealsUseCase: GetTenRandomPotatoMealsUseCase,
) {
    fun runApp() {
        showTenRandomPotatoMeals()
    }

    private fun showTenRandomPotatoMeals() {
        println("${ConsoleStyle.BOLD}${ConsoleColors.CYAN}This is very yummy, try this potato meals and Ed3yly.${ConsoleStyle.RESET}")

        getTenRandomPotatoMealsUseCase().forEach {
            printMeal(it)
        }
    }

    private fun printMeal(meal: Meal) {
        println(
            """
                -ID: ${meal.id}
            This recipe is called: ${meal.name},
            ${meal.description}
            
            Ingredients: ${meal.ingredients}
            
            ==============================================
            """.trimIndent(),
        )
    }
}
