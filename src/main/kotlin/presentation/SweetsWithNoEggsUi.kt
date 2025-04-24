package org.seoulsquad.presentation

import logic.model.Meal
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption

class SweetsWithNoEggsUi(
    private val getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase,
    private val mealPrinter: MealPrinter,
    private val viewer: Viewer,
    private val reader: Reader
) {
    fun startSweetsWithNoEggsFlow() {
        printSweetsWithNoEggsIntroductionMessage()
        getSweetsWithNoEggs()
    }

    private fun printSweetsWithNoEggsIntroductionMessage() {
        viewer.display("Looking for a sweet without eggs? You're in the right place!")
        viewer.display("Like to see more details, or dislike to get another suggestion.")
        viewer.display("Loading, Please wait...")
    }

    private fun getSweetsWithNoEggs() {
        getSweetsWithNoEggsUseCase()
            .onSuccess { sweetsList ->
                suggestMeal(sweetsList)
            }.onFailure { e ->
                viewer.display(e.message)
            }
    }
    private fun suggestMeal(meals: List<Meal>) {
        if (meals.isEmpty()) {
            viewer.display(OUT_OF_MEALS_MESSAGE)
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
                    viewer.display(INVALID_OPTION_MESSAGE)
                }
            }
        }
    }
    companion object{
        const val INVALID_OPTION_MESSAGE = "Invalid option"
        const val OUT_OF_MEALS_MESSAGE = "We are out of meals for now."
    }
}