package org.seoulsquad.presentation

import org.seoulsquad.logic.model.MealDate
import org.seoulsquad.logic.useCase.GetMealUsingIDUseCase
import org.seoulsquad.logic.useCase.SearchFoodsUsingDateUseCase
import org.seoulsquad.presentation.utils.SharedFunctions

class SearchMealUsingDateUi(
    private val getMealUsingIDUseCase: GetMealUsingIDUseCase,
    private val searchFoodsUsingDateUseCase: SearchFoodsUsingDateUseCase,
) {
    fun searchMealUsingDate() {
        println("Enter a date to search for meals (format: MM-DD-YYYY):")
        val inputDate = readln()
        println("Loading................")

        searchFoodsUsingDateUseCase(inputDate)
            .onSuccess { meals ->
                displayMealListOfSearchedDate(meals, inputDate)
                fetchMealAccordingID(meals)
            }.onFailure { e ->
                println("\n Error searching meals: ${e.message}")
            }
    }

    private fun displayMealListOfSearchedDate(
        meals: List<MealDate>,
        inputDate: String,
    ) {
        println("\n Found ${meals.size} meal(s) submitted on $inputDate:\n")
        meals.forEachIndexed { index, meal ->
            println("${index + 1}. [ID: ${meal.id}] ${meal.nameOfMeal} (${meal.date})")
        }
    }

    private fun fetchMealAccordingID(dateMeals: List<MealDate>) {
        println("\n If you'd like to view details for a specific meal, enter the Meal ID:")
        val mealId = readln()
        getMealUsingIDUseCase(mealId, dateMeals)
            .onSuccess { meals ->
                SharedFunctions.printFullMeal(meals)
            }
            .onFailure { e ->
                println("\n Could not retrieve meal details: ${e.message}")
            }
    }

}