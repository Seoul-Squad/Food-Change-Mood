package org.seoulsquad.presentation

import logic.model.Meal
import logic.useCase.GetRandomPotatoMealsUseCase
import org.seoulsquad.presentation.utils.SharedFunctions
import presentation.utils.ConsoleColors
import presentation.utils.ConsoleStyle

class ShowRandomPotatoMealsUi(
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase
) {
    fun startShowRandomPotatoMeals() {
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
            Thread.sleep(1000)
            println("But cheers up he have: ${mealsWithPotato.size}\uD83D\uDE03")
            Thread.sleep(1500)
            println("So here is ${mealsWithPotato.size} potato meals instead\uD83D\uDE09\n\n")
            Thread.sleep(1000)
        }
        mealsWithPotato.forEach { SharedFunctions.printMeal(it) }

        getUserPotatoInterestMeal(mealsWithPotato)
    }

    private fun getUserPotatoInterestMeal(mealsWithPotato: List<Meal>) {
        println("Are you interest in any of this meals? (y/n)")
        var userInput = readlnOrNull() ?: ""

        if (userInput == "y") {
            println("Please enter meal id: ")
            userInput = readlnOrNull() ?: ""
            SharedFunctions.printFullMeal(mealsWithPotato.first { it.id == userInput.toInt() })
            println("Bon-appetit\uD83D\uDE09")
        }
    }
}