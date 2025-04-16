package presentation

import logic.model.Meal
import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.useCase.GetRandomEasyMealsUseCase
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption

class ConsoleUi(
    private val getRandomEasyMealsUseCase: GetRandomEasyMealsUseCase,
    private val exploreOtherCountriesFoodUseCase: ExploreOtherCountriesFoodUseCase,
    private val getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase,
) {
    fun start() {
        when (getUserInput()) {
            "4" ->  printRandomEasyMeals()
            "6"->startSweetsWithNoEggsFlow()
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
            exploreOtherCountriesFoodUseCase.findMealsByCountry(it)
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
    private fun startSweetsWithNoEggsFlow() {
        printSweetsWithNoEggsIntroductionMessage()
        getSweetsWithNoEggs()
    }

    private fun printSweetsWithNoEggsIntroductionMessage() {
        println("Looking for a sweet without eggs? You're in the right place!")
        println("Like to see more details, or dislike to get another suggestion.")
        println("Loading, Please wait...")
    }

    private fun getSweetsWithNoEggs() {
        getSweetsWithNoEggsUseCase
            .getSweetsWithNoEggs()
            .onSuccess { sweetsList ->
                suggestMeal(sweetsList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }

    private fun suggestMeal(meals: List<Meal>) {
        if (meals.isEmpty()) {
            println("We are out of meals for now!")
            return
        }
        val randomMeal = meals.random()
        printShortMeal(randomMeal)
        printLikeAndDislikeOptions()
        handleSuggestionFeedback(randomMeal, meals)
    }

    private fun handleSuggestionFeedback(
        randomMeal: Meal,
        meals: List<Meal>,
    ) {
        when (readln().toIntOrNull()) {
            SuggestionFeedbackOption.LIKE.ordinal -> {
                printFullMeal(randomMeal)
            }

            SuggestionFeedbackOption.DISLIKE.ordinal -> {
                suggestMeal(meals.minusElement(randomMeal))
            }

            else -> {
                println("Please, enter a valid option!")
                suggestMeal(meals)
            }
        }
    }

    private fun printLikeAndDislikeOptions() {
        SuggestionFeedbackOption.entries.forEach { println("${it.ordinal}. ${it.title}") }
    }

    private fun printShortMeal(meal: Meal) {
        println("\u001B[1mMeal: ${meal.name}\u001B[0m")
        meal.description.takeIf { !it.isNullOrBlank() }?.run { println(this) }
    }

    private fun printFullMeal(meal: Meal) {
        with(meal) {
            println("Meal: $name (ID: $id)")
            println("Time to Prepare: $minutes minutes")
            meal.description.takeIf { !it.isNullOrBlank() }.run { println("$this") }
            println("Ingredients ($numberOfIngredients):")
            ingredients.forEachIndexed { index, ingredient ->
                println("  ${index + 1}. $ingredient")
            }
            println("Steps ($numberOfSteps):")
            steps.forEachIndexed { index, step ->
                println("  Step ${index + 1}: $step")
            }
            println("Nutrition:")
            println("  - Calories: ${nutrition.calories} kcal")
            println("  - Total Fat: ${nutrition.totalFat} g")
            println("  - Saturated Fat: ${nutrition.saturatedFat} g")
            println("  - Sugar: ${nutrition.sugar} g")
            println("  - Sodium: ${nutrition.sodium} mg")
            println("  - Protein: ${nutrition.protein} g")
            println("  - Carbohydrates: ${nutrition.carbohydrates} g")
        }
    }

    private fun printRandomEasyMeals() {
        val result = getRandomEasyMealsUseCase()

        result.onSuccess { randomEasyMealsList ->
            randomEasyMealsList.forEach { meal ->
                printMeal(meal)
            }
        }.onFailure { exception->
            println(exception.message)
        }

    }


    private fun printMeal(meal: Meal) {
        with(meal){
            println("Meal: $name")
            println("Prepare time: $minutes minutes")

            println("Ingredients:")
            ingredients.forEachIndexed { index, ingredient->
                println("${index+1}- $ingredient")
            }

            println("Steps:")
            steps.forEachIndexed { index, step ->
                println("${index+1}- $step")
            }
        }
    }
}
