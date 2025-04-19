package org.seoulsquad.presentation

import logic.model.Meal
import logic.useCase.GetRandomPotatoMealsUseCase
import logic.utils.Constants.MAX_POTATO_MEALS
import logic.utils.NoMealsFoundException
import org.seoulsquad.presentation.utils.SharedFunctions
import org.seoulsquad.presentation.utils.SharedUi
import presentation.utils.ConsoleColors
import presentation.utils.ConsoleStyle

class ShowRandomPotatoMealsUi(
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase,
) {
    fun startShowRandomPotatoMeals() {
        getRandomPotatoMealsUseCase()
            .onSuccess { mealsWithPotato ->
                if (isNotEnoughMeals(mealsWithPotato)) println("We couldn't find $MAX_POTATO_MEALS meals containing potato.")
                println("\n\nHere is ${mealsWithPotato.size} meals containing potato.\n")
                printPotatoMealsResult(mealsWithPotato)
            }.onFailure { exception ->
                when (exception) {
                    is NoMealsFoundException -> println(exception.message)
                }
            }
    }

    private fun isNotEnoughMeals(mealsWithPotato: List<Meal>): Boolean = mealsWithPotato.size < MAX_POTATO_MEALS

    private fun printPotatoMealsResult(mealsWithPotato: List<Meal>) {
        mealsWithPotato.forEach { SharedFunctions.printFullMeal(it) }
    }
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