package org.seoulsquad.presentation

import logic.model.Meal
import org.seoulsquad.logic.useCase.GetMealsWithHighCaloriesUseCase
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption

class MealsWithHighCaloriesUi(
    private val getMealsWithHighCaloriesUseCase: GetMealsWithHighCaloriesUseCase,
    private val mealPrinter: MealPrinter,
    private val viewer: Viewer,
    private val reader: Reader
) {
    fun getMealsWithHighCalories() {
        getMealsWithHighCaloriesUseCase()
            .onSuccess { mealsList ->
                suggestMeal(mealsList)
            }.onFailure { e ->
                viewer.display("Error: ${e.message}")
            }
    }



    private fun suggestMeal(meals: List<Meal>) {
        if (meals.isEmpty()) {
            viewer.display("We are out of meals for now!")
            return
        }
        val randomMeal = meals.random()

        handleSuggestionFeedback(randomMeal, meals)
    }

    private fun handleSuggestionFeedback(
        randomMeal: Meal,
        meals: List<Meal>,
    ) {
        while(true) {
            mealPrinter.printShortMeal(randomMeal)
            mealPrinter.printLikeAndDislikeOptions()

            when (reader.readInt()) {
                SuggestionFeedbackOption.LIKE.ordinal -> {
                    return mealPrinter.printFullMeal(randomMeal)
                }

                SuggestionFeedbackOption.DISLIKE.ordinal -> {
                    return suggestMeal(meals.minusElement(randomMeal))
                }
                else ->{
                    viewer.display("Invalid option")
                }
            }
        }
    }
}