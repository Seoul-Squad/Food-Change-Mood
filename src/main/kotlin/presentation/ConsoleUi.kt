package presentation

import logic.model.Meal
import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.seoulsquad.logic.useCase.GetSearchByNameUseCase
import org.seoulsquad.logic.utils.KmpSearchAlgorithm
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption

class ConsoleUi(
    private val exploreOtherCountriesFoodUseCase: ExploreOtherCountriesFoodUseCase,
    private val getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase,
    private val getSearchByNameUseCase: GetSearchByNameUseCase,
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase
) {
    private fun searchByMealName() {
        print("Enter Meal Name:")
        val query = readlnOrNull() ?: ""
        println("Your search result")
        getSearchByNameUseCase.getSearchByName(query, KmpSearchAlgorithm()).onSuccess { meals ->
            printSearchResult(meals)
        }.onFailure { e ->
            println("Error: ${e.message}")
        }
    }

    private fun printSearchResult(meals: List<Meal>) {
        meals.forEach { printMeal(it) }
    }

    private fun printMeal(meal: Meal) {
        println(
            """
                -ID: ${meal.id}
            This recipe is called: ${meal.name},
            ${meal.description}
            
            Ingredients: ${meal.ingredients}
            
            ==============================================
            """.trimIndent(),
        )
    }


    fun start() {
        printMenu()
        when (getUserInput()) {
            "2"->searchByMealName()
            "6"->startSweetsWithNoEggsFlow()
            "10" -> exploreOtherCountriesFood()
            "3" -> startIraqiMealsFlow()
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
    fun startSweetsWithNoEggsFlow() {
        printSweetsWithNoEggsIntroductionMessage()
        getSweetsWithNoEggs()
    }

    private fun printMenu() {
        println("Choose a task")
        println("2. search by name")
        println("3. search by ID")
        println("10. search by ID")
        println("15. Italuan")
        println("Loading, Please wait...")
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
    fun startIraqiMealsFlow() {
        printIraqiMealsIntroductionMessage()
        getIraqiMeals()
    }
    private fun printIraqiMealsIntroductionMessage() {
        println("Looking for an Iraqi meal? You're in the right place!")
        println("Loading, Please wait...")
    }
    private fun getIraqiMeals(){
        getIraqiMealsUseCase.getAllIraqMeals()
            .onSuccess { mealsList ->
                mealsList.forEach {meal ->
                    printFullMeal(meal)
                    println("\n---\n")
                }
            }
            .onFailure { exception ->
                println(exception.message)
            }
    }
}
