package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetSearchByNameUseCase
import org.seoulsquad.logic.utils.KnuthMorrisPrattMealSearchAlgorithm
import org.seoulsquad.presentation.utils.SharedFunctions
import org.seoulsquad.logic.utils.KnuthMorrisPrattSearchAlgorithm
import org.seoulsquad.presentation.utils.SharedUi

class SearchByNameUi(
    private val getSearchByNameUseCase: GetSearchByNameUseCase
) {

    fun searchByMealName() {
        print("Enter Meal Name:")
        val query = readlnOrNull() ?: ""
        println("Your search result")
        getSearchByNameUseCase
            .getSearchByName(query)
            .onSuccess { meals ->
                SharedUi().printSearchResult(meals)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }


}

