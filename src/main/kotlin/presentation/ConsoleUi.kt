package presentation

import logic.model.Meal
import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.useCase.GetRandomEasyMealsUseCase
import logic.useCase.GuessGameUseCase
import logic.useCase.NegativeNumberException
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import org.seoulsquad.logic.useCase.GetMealUsingIDUseCase
import org.seoulsquad.logic.useCase.GetSearchByNameUseCase
import org.seoulsquad.logic.useCase.SearchFoodsUsingDateUseCase
import org.seoulsquad.logic.useCase.model.MealDate
import org.seoulsquad.logic.utils.KmpSearchAlgorithm
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption

class ConsoleUi(
    private val exploreOtherCountriesFoodUseCase: ExploreOtherCountriesFoodUseCase,
    private val getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase,
    private val getMealUsingIDUseCase: GetMealUsingIDUseCase,
    private val searchFoodsUsingDateUseCase: SearchFoodsUsingDateUseCase,
    private val getSearchByNameUseCase: GetSearchByNameUseCase,
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val guessGameUseCase: GuessGameUseCase,
    private val getRandomEasyMealsUseCase: GetRandomEasyMealsUseCase
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
            "3" -> startIraqiMealsFlow()
            "4" ->  printRandomEasyMeals()
            "5" -> startGuessGame()
            "6"->startSweetsWithNoEggsFlow()
            "8" -> searchMealUsingDate()
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
    fun startSweetsWithNoEggsFlow() {
        printSweetsWithNoEggsIntroductionMessage()
        getSweetsWithNoEggs()
    }

    private fun printMenu() {
        println("Choose a task")
        println("2. search by name")
        println("3. Iraqi Meals")
        println("4. Easy Meals")
        println("5. Guess Game")
        println("6. Sweets without eggs")
        println("8. search by date")
        println("10. explore other countries food")
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
            meal.description.takeIf { !it.isNullOrBlank() }?.run { println(this) }
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
    private fun startIraqiMealsFlow() {
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
    private fun printRandomEasyMeals() {
        val result = getRandomEasyMealsUseCase()

        result.onSuccess { randomEasyMealsList ->
            randomEasyMealsList.forEach { meal ->
                printFullMeal(meal)
            }
        }.onFailure { exception->
            println(exception.message)
        }

    }


    private fun searchMealUsingDate() {
        println("Enter a date to search for meals (format: MM-DD-YYYY):")
        val inputDate = readln()
        println("Loading................")

        searchFoodsUsingDateUseCase(inputDate)
            .onSuccess { meals ->
                displayMealListOfSearchedDate(meals, inputDate)
                fetchMealAccordingID()
            }
            .onFailure { e ->
                println("\n Error searching meals: ${e.message}")
            }
    }

    private fun displayMealListOfSearchedDate(meals: List<MealDate>, inputDate: String) {
        println("\n Found ${meals.size} meal(s) submitted on $inputDate:\n")
        meals.forEachIndexed { index, meal ->
            println("${index + 1}. [ID: ${meal.id}] ${meal.nameOfMeal} (${meal.date})")
        }
    }

    private fun fetchMealAccordingID() {
        println("\n If you'd like to view details for a specific meal, enter the Meal ID:")
        val mealId = readln()
        getMealUsingIDUseCase(mealId)
            .onSuccess { meals ->
                meals.forEach { meal -> printFullMeal(meal) }
            }
            .onFailure { e ->
                println("\n Could not retrieve meal details: ${e.message}")
            }
    }



    private fun startGuessGame() {
        do {
            val meal = guessGameUseCase.generateRandomMeal()
            if (meal == null) {
                println("No meals available!")
                return
            }

            println("Guess the preparation time (in minutes) for: ${meal.name}")

            var attempts = 0
            var isCorrect = false

            while (attempts < 3 && !isCorrect) {
                println("Attempt ${attempts + 1}:")
                print("Enter your guess: ")

                val input = readlnOrNull()
                try {
                    val guess = guessGameUseCase.userGuess(input)

                    isCorrect = guessGameUseCase.guessIsCorrect(guess, meal.minutes)

                    when {
                        isCorrect -> println("Correct! You guessed the right time!")
                        guessGameUseCase.guessIsTooHigh(guess, meal.minutes) -> println("Too high!")
                        guessGameUseCase.guessIsTooLow(guess, meal.minutes) -> println("Too low!")
                    }
                } catch (e: IllegalArgumentException) {
                    println("Invalid input: ${e.message}")
                } catch (e: NegativeNumberException) {
                    println("Invalid input: ${e.message}")
                }

                attempts++
            }

            if (!isCorrect) {
                println("You got it wrong, better luck next time! The correct time was ${meal.minutes} minutes.")
            }

            println("\nDo you want to play again? (y/n)")
            val playAgain = readlnOrNull()?.lowercase() == "y"

        } while (playAgain)
    }

}
