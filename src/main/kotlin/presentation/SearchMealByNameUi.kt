package org.seoulsquad.presentation

import logic.utils.Constants.EXIT
import org.seoulsquad.logic.useCase.SearchMealByNameUseCase
import org.seoulsquad.logic.utils.KnuthMorrisPrattSearchAlgorithm
import org.seoulsquad.presentation.utils.SharedFunctions

class SearchMealByNameUi (
    private val searchMealByNameUseCase: SearchMealByNameUseCase
    ){

    fun searchMealByName() {
        println("Welcome to the Meal Searcher!")
        while (true){
            println("Please enter a meal name to search for (or type '$EXIT' to quit):")
            val query = readlnOrNull() ?: ""
            if (query == EXIT) {
                println("Exiting the Meal Searcher. Goodbye!")
                return
            }
            searchMealByNameUseCase
                .getSearchByName(query, KnuthMorrisPrattSearchAlgorithm())
                .onSuccess { meals ->
                    println("Your search result")
                    SharedFunctions.printSearchResult(meals)
                }.onFailure { e ->
                    println("Error: ${e.message}")
                }
        }
    }

}

