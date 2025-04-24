package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.SearchMealsByNameUseCase
import org.seoulsquad.presentation.utils.MealPrinter

class SearchByNameUi(
    private val getSearchByNameUseCase: SearchMealsByNameUseCase,
    private val mealPrinter: MealPrinter
) {

    fun searchByMealName() {
        print("Enter Meal Name:")
        val query = readlnOrNull() ?: ""
        println("Your search result")
        getSearchByNameUseCase(query)
            .onSuccess { meals ->
                mealPrinter.printSearchResult(meals)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }


}

