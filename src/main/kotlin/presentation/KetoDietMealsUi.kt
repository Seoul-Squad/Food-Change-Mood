package org.seoulsquad.presentation

import logic.model.Meal
import logic.useCase.GetKetoDietMealUseCase
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption

class KetoDietMealsUi(
    private val getKetoDietMealUseCase: GetKetoDietMealUseCase,
    private val mealPrinter: MealPrinter,
    private val viewer: Viewer,
    private val reader: Reader
) {
    fun startKetoDietFlow() {
        printKetoIntroMessage()
        getKetoMeals()
    }

    private fun printKetoIntroMessage() {
        viewer.display("Following a Keto diet? We’ve got some low-carb options for you!")
        viewer.display("You can like to see full details or dislike to get another meal.")
        viewer.display("Loading Keto meals, please wait...")
    }

    private fun getKetoMeals() {
        getKetoDietMealUseCase
            .getKetoDietMeal()
            .onSuccess { ketoList ->
                suggestMeal(ketoList)
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