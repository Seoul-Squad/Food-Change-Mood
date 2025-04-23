package org.seoulsquad.presentation

import logic.model.Meal
import logic.useCase.GetRandomPotatoMealsUseCase
import logic.utils.Constants.MAX_POTATO_MEALS
import logic.utils.NoMealsFoundException
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.SharedUi

class ShowRandomPotatoMealsUi(
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase,
    private val viewer: Viewer,
) {
    fun startShowRandomPotatoMeals() {
        getRandomPotatoMealsUseCase()
            .onSuccess { mealsWithPotato ->
                if (isNotEnoughMeals(mealsWithPotato)) viewer.display("We couldn't find $MAX_POTATO_MEALS meals containing potato.")
                viewer.display("\n\nHere is ${mealsWithPotato.size} meals containing potato.\n")
                printPotatoMealsResult(mealsWithPotato)
            }.onFailure { exception ->
                when (exception) {
                    is NoMealsFoundException -> exception.message?.let { viewer.display(it) }
                }
            }
    }

    private fun isNotEnoughMeals(mealsWithPotato: List<Meal>): Boolean = mealsWithPotato.size < MAX_POTATO_MEALS

    private fun printPotatoMealsResult(mealsWithPotato: List<Meal>) {
        mealsWithPotato.forEach { SharedUi().printFullMeal(it) }
    }
}
