package org.seoulsquad.presentation

import logic.utils.Constants.EXIT
import org.seoulsquad.logic.model.MealDate
import org.seoulsquad.logic.useCase.GetMealUsingIDUseCase
import org.seoulsquad.logic.useCase.SearchFoodsUsingDateUseCase
import org.seoulsquad.logic.utils.toLocalDate
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter

class SearchMealUsingDateUi(
    private val getMealUsingIDUseCase: GetMealUsingIDUseCase,
    private val searchFoodsUsingDateUseCase: SearchFoodsUsingDateUseCase,
    private val viewer: Viewer,
    private val reader: Reader,
    private val mealPrinter: MealPrinter
) {


    fun searchMealUsingDate() {
        while (true) {
            viewer.display("Enter a date to search for meals (format: MM-DD-YYYY): (or type '$EXIT' to quit)")


            when (val input = reader.readString()) {
                EXIT -> return viewer.display("Exiting the Food Explorer")
                else -> {
                    viewer.display("Loading................")

                    searchFoodsUsingDateUseCase(input.toLocalDate())

                        .onSuccess { meals ->
                            displayMealListOfSearchedDate(meals, input)
                            fetchMealAccordingID(meals)
                        }.onFailure { e ->
                            viewer.display("\n Error searching meals: ${e.message}")
                        }
                }
            }
        }
    }

    private fun displayMealListOfSearchedDate(
        meals: List<MealDate>,
        inputDate: String,
    ) {
        viewer.display("\n Found ${meals.size} meal(s) submitted on $inputDate:\n")
        meals.forEachIndexed { index, meal ->
            viewer.display("${index + 1}. [ID: ${meal.id}] ${meal.nameOfMeal} (${meal.date})")
        }
    }

    private fun fetchMealAccordingID(dateMeals: List<MealDate>) {
        while (true) {
            viewer.display("\n If you'd like to view details for a specific meal, enter the Meal ID: (or type '$EXIT' to quit)")
            when (val input = reader.readInt()) {
                EXIT.toInt() -> return viewer.display("Exiting the Food Explorer")
                else -> {
                    getMealUsingIDUseCase(input, dateMeals)
                        .onSuccess { meals ->
                            mealPrinter.printFullMeal(meals)
                        }.onFailure { e ->
                            viewer.display("\n Could not retrieve meal details: ${e.message}")
                        }
                }
            }

        }
    }
}