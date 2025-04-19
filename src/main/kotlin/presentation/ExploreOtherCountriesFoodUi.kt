package org.seoulsquad.presentation

import logic.useCase.ExploreCountryMealsUseCase

class ExploreOtherCountriesFoodUi(
    private val exploreOtherCountriesFoodUseCase: ExploreCountryMealsUseCase,
) {
     fun exploreOtherCountriesFood() {
        println("Welcome to the Food Explorer!")
        println("Please enter a country name to explore its food:")
        val country = readlnOrNull()
        country?.let {
            exploreOtherCountriesFoodUseCase(it, 40)
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