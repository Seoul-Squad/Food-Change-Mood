package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetSearchByNameUseCase
import org.seoulsquad.logic.utils.KmpSearchAlgorithm
import org.seoulsquad.presentation.utils.SharedFunctions
import presentation.ConsoleUi

class SearchByNameUi (
    private val getSearchByNameUseCase: GetSearchByNameUseCase
    ){

    public fun searchByMealName() {
        print("Enter Meal Name:")
        val query = readlnOrNull() ?: ""
        println("Your search result")
        getSearchByNameUseCase
            .getSearchByName(query, KmpSearchAlgorithm())
            .onSuccess { meals ->
                SharedFunctions.printSearchResult(meals)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }


}

