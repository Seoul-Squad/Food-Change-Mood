package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.SearchMealsByNameUseCase
import org.seoulsquad.presentation.utils.SharedUi

class SearchByNameUi(
    private val getSearchByNameUseCase: SearchMealsByNameUseCase
) {

    fun searchByMealName() {
        print("Enter Meal Name:")
        val query = readlnOrNull() ?: ""
        println("Your search result")
        getSearchByNameUseCase(query)
            .onSuccess { meals ->
                SharedUi().printSearchResult(meals)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }


}

