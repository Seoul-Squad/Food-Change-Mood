package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetSearchByNameUseCase
import org.seoulsquad.logic.utils.KnuthMorrisPrattSearchAlgorithm
import org.seoulsquad.presentation.utils.SharedFunctions

class SearchByNameUi (
    private val getSearchByNameUseCase: GetSearchByNameUseCase
    ){

    fun searchByMealName() {
        print("Enter Meal Name:")
        val query = readlnOrNull() ?: ""
        println("Your search result")
        getSearchByNameUseCase
            .getSearchByName(query, KnuthMorrisPrattSearchAlgorithm())
            .onSuccess { meals ->
                SharedFunctions.printSearchResult(meals)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }


}

