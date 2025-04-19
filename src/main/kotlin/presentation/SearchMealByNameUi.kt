package org.seoulsquad.presentation

import logic.utils.Constants.EXIT
import org.seoulsquad.logic.useCase.SearchMealsByNameUseCase
import org.seoulsquad.presentation.utils.SharedUi

class SearchMealByNameUi(
    private val searchMealByNameUseCase: SearchMealsByNameUseCase
) {

    fun searchMealByName() {
        println("Welcome to the Meal Searcher!")
        while (true) {
            println("Please enter a meal name to search for (or type '$EXIT' to quit):")
            val query = readlnOrNull() ?: ""
            if (query == EXIT) {
                println("Exiting the Meal Searcher. Goodbye!")
                return
            }
            searchMealByNameUseCase(query)
                .onSuccess { meals ->
                    println("Your search result")
                    SharedUi().printSearchResult(meals)
                }.onFailure { e ->
                    println("Error: ${e.message}")
                }
        }
    }

}

