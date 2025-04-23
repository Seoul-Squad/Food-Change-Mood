package org.seoulsquad.presentation

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
        while (true) {
            viewer.display("\n Please enter a country name to explore its food (or type '$EXIT' to quit):")
            val country = reader.readString()
            if (country == EXIT) {
                viewer.display("Exiting the Food Explorer. Goodbye!")
                return
            }
            country?.let {
                exploreOtherCountriesFoodUseCase(it)
                    .onSuccess { meals ->
                        viewer.display("Here are some meals from $country:")
                        meals.forEach { meal ->
                            viewer.display("- ${meal.name}: ${meal.description}")
                        }
                    }.onFailure { error ->
                        viewer.display("Error: ${error.message}")
                    }
            }
        }
    }
}