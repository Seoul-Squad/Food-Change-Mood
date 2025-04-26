package org.seoulsquad.presentation

import logic.model.Meal
import logic.useCase.GetRandomMealsByIngredientUseCase
import logic.utils.Constants.DEFAULT_INGREDIENT
import logic.utils.Constants.MAX_MEALS
import logic.utils.NoMealsFoundException
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter

class ShowRandomMealsByIngredientUi(
    private val getRandomMealsByIngredientUseCase: GetRandomMealsByIngredientUseCase,
    private val mealPrinter: MealPrinter,
    private val viewer: Viewer,
) {
    fun startShowRandomMealsByIngredient() {
        getRandomMealsByIngredientUseCase(DEFAULT_INGREDIENT)
            .onSuccess { meals ->
                if (isNotEnoughMeals(meals)) viewer.display("We couldn't find $MAX_MEALS meals containing $DEFAULT_INGREDIENT.")
                viewer.display("\n\nHere is ${meals.size} meals containing $DEFAULT_INGREDIENT.\n")
                printMealsResult(meals)
            }.onFailure { exception ->
                handleException(exception)
            }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is NoMealsFoundException -> viewer.display(exception.message)
            else -> viewer.display("An error occurred: ${exception.message}")
        }
    }

    private fun isNotEnoughMeals(meals: List<Meal>): Boolean = meals.size < MAX_MEALS

    private fun printMealsResult(meals: List<Meal>) {
        meals.forEach { mealPrinter.printFullMeal(it) }
    }
}
