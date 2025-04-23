package org.seoulsquad.presentation

import logic.model.Meal
import logic.useCase.ExploreCountryMealsUseCase
import logic.utils.Constants.EXIT
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer

class ExploreOtherCountriesFoodUi(
    private val exploreOtherCountriesFoodUseCase: ExploreCountryMealsUseCase,
    private val reader: Reader,
    private val viewer: Viewer,
) {
    fun exploreOtherCountriesFood() {
        viewer.display("Welcome to the Food Explorer!")
        loop()
    }

    private fun loop() {
        viewer.display("\n Please enter a country name to explore its food (or type '$EXIT' to quit):")

        when (val countryName = reader.readString()) {
            EXIT -> viewer.display("Exiting the Food Explorer. Goodbye()")
            else -> {
                exploreOtherCountriesFoodUseCase(countryName)
                    .onSuccess { meals ->
                        displayMeals(countryName, meals)
                    }
                    .onFailure { error ->
                        viewer.display("Error: ${error.message}")
                    }
                loop()
            }
        }
    }

    private fun displayMeals(country: String, meals: List<Meal>) {
        viewer.display("Here are some meals from $country:")
        meals.forEach { meal ->
            viewer.display("- ${meal.name}: ${meal.description}")
        }
    }

}