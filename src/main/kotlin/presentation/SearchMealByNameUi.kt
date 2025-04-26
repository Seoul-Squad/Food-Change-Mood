package org.seoulsquad.presentation

import logic.utils.Constants.EXIT
import org.seoulsquad.logic.useCase.SearchMealsByNameUseCase
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter

class SearchMealByNameUi(
    private val searchMealByNameUseCase: SearchMealsByNameUseCase,
    private val reader: Reader,
    private val viewer: Viewer,
    private val mealPrinter: MealPrinter
) {

    fun searchMealByName() {
        viewer.display("Welcome to the Meal Searcher!")
        while (true) {
            viewer.display("Please enter a meal name to search for (or type '$EXIT' to quit):")
            when (val query = reader.readString()) {
                EXIT -> {
                    viewer.display("Exiting the Meal Searcher. Goodbye!")
                    break
                }

                else -> {
                    searchMealByNameUseCase(query)
                        .onSuccess { meals ->
                            viewer.display("Your search result")
                            mealPrinter.printSearchResult(meals)
                        }.onFailure { e ->
                            viewer.display("Error: ${e.message}")
                        }
                }
            }
        }
    }

}

