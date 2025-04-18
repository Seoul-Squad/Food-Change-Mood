package org.seoulsquad.presentation

import logic.useCase.ExploreCountryMealsUseCase
import logic.utils.Constants.EXIT

class ExploreOtherCountriesFoodUi(
    private val exploreOtherCountriesFoodUseCase: ExploreCountryMealsUseCase,
) {
     fun exploreOtherCountriesFood() {
        println("Welcome to the Food Explorer!")
         while(true){
            println("\nPlease enter a country name to explore its food (or type '$EXIT' to quit):")
            val country = readlnOrNull()
            if (country == EXIT) {
                println("Exiting the Food Explorer. Goodbye!")
                return
            }
            country?.let {
                exploreOtherCountriesFoodUseCase(it)
                    .onSuccess { meals ->
                        println("Here are some meals from $country:")
                        meals.forEach { meal ->
                            println("- ${meal.name}: ${meal.description}")
                        }
                    }.onFailure { error ->
                        println("Oops: ${error.message}")
                    }
            }
         }
    }
}