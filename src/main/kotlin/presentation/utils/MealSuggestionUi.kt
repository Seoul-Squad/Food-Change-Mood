package org.seoulsquad.presentation.utils

import logic.model.Meal
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer

class MealSuggestionUi (
    private val mealPrinter: MealPrinter,
    private val viewer: Viewer,
    private val reader: Reader
){
    fun suggestMeal(meals: List<Meal>) {
        if (meals.isEmpty()) {
            viewer.display("We are out of meals for now!")
            return
        }
        val randomMeal = meals.random()

        handleSuggestionFeedback(randomMeal, meals)
    }

    fun handleSuggestionFeedback(
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