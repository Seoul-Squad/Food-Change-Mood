package presentation

import logic.useCase.ExploreOtherCountriesFoodUseCase

class ConsoleUi(
    private val exploreOtherCountriesFoodUseCase: ExploreOtherCountriesFoodUseCase
) {
    fun start() {
        when (getUserInput()) {
            "10" -> exploreOtherCountriesFood()
            else -> println("Invalid option. Please try again.")
        }
    }

    private fun getUserInput(): String {
        return readlnOrNull() ?: ""
    }

    private fun exploreOtherCountriesFood() {
        println("Welcome to the Food Explorer!")
        println("Please enter a country name to explore its food:")
        val country = readlnOrNull()
        country?.let {
            exploreOtherCountriesFoodUseCase.findMealsByCountry(it,40)
                .onSuccess { meals ->
                    println("Here are some meals from $country:")
                    meals.forEach { meal ->
                        println("- ${meal.name}: ${meal.description}")
                    }
                }
                .onFailure { error ->
                    println("Oops: ${error.message}")
                }
        }
    }


}
