package presentation

import GetItalianLargeMealsUseCase
import logic.model.Meal
import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetKetoDietMealUseCase
import logic.useCase.GetRandomEasyMealsUseCase
import logic.useCase.GetRandomPotatoMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.useCase.GuessGameUseCase
import logic.useCase.NegativeNumberException
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import org.seoulsquad.logic.useCase.GetMealUsingIDUseCase
import org.seoulsquad.logic.useCase.GetMealsWithHighCaloriesUseCase
import org.seoulsquad.logic.useCase.GetSearchByNameUseCase
import org.seoulsquad.logic.useCase.GetSortedSeafoodMealsUseCase
import org.seoulsquad.logic.useCase.SearchFoodsUsingDateUseCase
import org.seoulsquad.logic.useCase.model.MealDate
import org.seoulsquad.logic.utils.KmpSearchAlgorithm
import org.seoulsquad.presentation.IngredientGame
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption
import presentation.utils.ConsoleColors
import presentation.utils.ConsoleStyle
import kotlin.collections.forEachIndexed
import kotlin.math.max

class ConsoleUi(
    private val exploreOtherCountriesFoodUseCase: ExploreOtherCountriesFoodUseCase,
    private val getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase,
    private val getKetoDietMealUseCase: GetKetoDietMealUseCase,
    private val getMealUsingIDUseCase: GetMealUsingIDUseCase,
    private val searchFoodsUsingDateUseCase: SearchFoodsUsingDateUseCase,
    private val getSearchByNameUseCase: GetSearchByNameUseCase,
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val getMealsWithHighCaloriesUseCase: GetMealsWithHighCaloriesUseCase,
    private val guessGameUseCase: GuessGameUseCase,
    private val getRandomEasyMealsUseCase: GetRandomEasyMealsUseCase,
    private val getItalianLargeMealsUseCase: GetItalianLargeMealsUseCase,
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase,
    private val getSortedSeafoodMealsUseCase: GetSortedSeafoodMealsUseCase,
    //
    private val ingredientGame: IngredientGame,
) {
    fun start() {
        printMenu()
        when (getUserInput()) {
            "2" -> searchByMealName()
            "3" -> startIraqiMealsFlow()
            "4" -> printRandomEasyMeals()
            "5" -> startGuessGame()
            "6" -> startSweetsWithNoEggsFlow()
            "7" -> startKetoDietFlow()
            "8" -> searchMealUsingDate()
            "10" -> exploreOtherCountriesFood()
            "11" -> ingredientGame.startIngredientGame()
            "12" -> startShowRandomPotatoMeals()
            "13" -> getMealsWithHighCalories()
            "14" -> startSeafoodMealsSortedByProtein()
            "15" -> startItalianLargeMealsFlow()

            else -> println("Invalid option. Please try again.")
        }
    }

    private fun printMenu() {
        println("Choose a task")
        println("2. search by name")
        println("3. Iraqi Meals")
        println("4. Easy Meals")
        println("5. Guess Game")
        println("6. Sweets without eggs")
        println("7. Keto Diet Meals")
        println("8. search by date")
        println("10. explore other countries food")
        println("11. Ingredient Game")
        println("12. Show 10 random meals contains potato")
        println("13. Meals with high calories")
        println("14. Seafood meals sorted by protein content")
        println("15. Italian Large Meals")
    }

    private fun getUserInput(): String = readlnOrNull() ?: ""

    // //////////////////////////////////////////////////////////////////////////////////
    private fun searchByMealName() {
        print("Enter Meal Name:")
        val query = readlnOrNull() ?: ""
        println("Your search result")
        getSearchByNameUseCase
            .getSearchByName(query, KmpSearchAlgorithm())
            .onSuccess { meals ->
                printSearchResult(meals)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }

    private fun printMeal(meal: Meal) {
        println(
            """
            -ID: ${meal.id}
                This recipe is called: ${meal.name},
            ${meal.description}
            
            ==============================================
            """.trimIndent(),
        )
    }

    private fun startItalianLargeMealsFlow() {
        printItalianLargeMealsIntroductionMessage()
        getItalianLargeMeals()
    }

    private fun printItalianLargeMealsIntroductionMessage() {
        println(
            """Are you a large group of friends traveling to Italy?
            | Do you want to share a meal?",
            |Here The suggestion
            """.trimMargin(),
        )
    }

    private fun getItalianLargeMeals() {
        getItalianLargeMealsUseCase
            .getItalianLargeMeals()
            .onSuccess { italianMeals ->
                printSearchResult(italianMeals)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }

    private fun printSearchResult(meals: List<Meal>) {
        meals.forEach { printMeal(it) }
    }

    // //////////////////////////////////////////////////////////////////////////////////

    private fun exploreOtherCountriesFood() {
        println("Welcome to the Food Explorer!")
        println("Please enter a country name to explore its food:")
        val country = readlnOrNull()
        country?.let {
            exploreOtherCountriesFoodUseCase
                .findMealsByCountry(it, 40)
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

    // //////////////////////////////////////////////////////////////////////////////////////

    private fun startSeafoodMealsSortedByProtein() {
        println("Loading, Please wait...")
        getSortedSeafoodMealsUseCase(
            compareByDescending<Meal> { it.nutrition.protein }.thenBy { it.name },
        ).onSuccess(::printMealsProteinTable).onFailure { e ->
            println("Error: ${e.message}")
        }
    }

    private fun printMealsProteinTable(meals: List<Meal>) {
        val indexColumnWidth = meals.size.toString().length
        val nameColumnWidth = maxOf(MEAL_NAME_HEADER.length, meals.maxOf { it.name.length })
        val proteinColumnWidth =
            max(
                MEAL_PROTEIN_HEADER.length,
                meals.maxOf {
                    it.nutrition.protein
                        .toString()
                        .length
                },
            )
        val paginatedMeals = meals.chunked(TABLE_PAGE_SIZE)
        paginatedMeals.forEachIndexed { index, mealsPage ->
            printMealProteinTablePage(
                pagesCount = paginatedMeals.size,
                pageIndex = index,
                meals = mealsPage,
                indexColumnWidth = indexColumnWidth,
                nameColumnWidth = nameColumnWidth,
                proteinColumnWidth = proteinColumnWidth,
            )
            if (doesUserWantToExit(index, paginatedMeals)) return
        }
    }

    private fun doesUserWantToExit(
        index: Int,
        paginatedMeals: List<List<Meal>>,
    ): Boolean {
        if (index != paginatedMeals.size - 1) {
            while (true) {
                println("Press Enter to view next page or 0 to exit.")
                when (readln().trim()) {
                    "" -> break
                    "0" -> return true
                    else -> println("Invalid input, Please retry again")
                }
            }
        }
        return false
    }

    private fun printMealProteinTablePage(
        pagesCount: Int,
        pageIndex: Int,
        meals: List<Meal>,
        indexColumnWidth: Int,
        nameColumnWidth: Int,
        proteinColumnWidth: Int,
    ) {
        val separatorLine =
            "|" + "-".repeat(indexColumnWidth + 2) + "|" + "-".repeat(nameColumnWidth + 2) + "|" +
                "-".repeat(
                    proteinColumnWidth + 2,
                ) + "|"
        println(separatorLine)
        println(
            "| ${"#".padEnd(indexColumnWidth, ' ')} | ${MEAL_NAME_HEADER.padEnd(nameColumnWidth, ' ')} | ${
                MEAL_PROTEIN_HEADER.padEnd(
                    proteinColumnWidth,
                    ' ',
                )
            } |",
        )
        println(separatorLine)
        meals.forEachIndexed { index, meal ->
            println(
                "| ${
                    ((index + 1) + (pageIndex * TABLE_PAGE_SIZE)).toString().padEnd(indexColumnWidth, ' ')
                } | ${meal.name.padEnd(nameColumnWidth, ' ')} | ${
                    meal.nutrition.protein.toString().padEnd(
                        proteinColumnWidth,
                        ' ',
                    )
                } |",
            )
        }
        println(separatorLine)
        println("=== Page ${pageIndex + 1} of $pagesCount ===")
    }

    // ///////////////////////////////////////////////
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
        }
    }

    // ////////////////////////////////////////////////

    fun startKetoDietFlow() {
        printKetoIntroMessage()
        getKetoMeals()
    }

    private fun printKetoIntroMessage() {
        println("Following a Keto diet? We’ve got some low-carb options for you!")
        println("You can like to see full details or dislike to get another meal.")
        println("Loading Keto meals, please wait...")
    }

    private fun getKetoMeals() {
        getKetoDietMealUseCase
            .getKetoDietMeal()
            .onSuccess { ketoList ->
                suggestMeal(ketoList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }

    private fun printLikeAndDislikeOptions() {
        SuggestionFeedbackOption.entries.forEach {
            println("${it.ordinal}. ${it.title}")
        }
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
        println("\n===========================================================\n")
    }

    // //////////////////////////////////////////////////////////////////////////////////////

    private fun getMealsWithHighCalories() {
        getMealsWithHighCaloriesUseCase()
            .onSuccess { mealsList ->
                suggestMeal(mealsList)
            }.onFailure { e ->
                println("Error: ${e.message}")
            }
    }

    // //////////////////////////////////////////////////////////////////////////////////////

    private fun startIraqiMealsFlow() {
        printIraqiMealsIntroductionMessage()
        getIraqiMeals()
    }

    private fun printIraqiMealsIntroductionMessage() {
        println("Looking for an Iraqi meal? You're in the right place!")
        println("Loading, Please wait...")
    }

    private fun getIraqiMeals() {
        getIraqiMealsUseCase
            .getAllIraqMeals()
            .onSuccess { mealsList ->
                mealsList.forEach { meal ->
                    printFullMeal(meal)
                    println("\n---\n")
                }
            }.onFailure { exception ->
                println(exception.message)
            }
    }

    // //////////////////////////////////////////////////////////////////////////////////////

    private fun printRandomEasyMeals() {
        val result = getRandomEasyMealsUseCase()

        result
            .onSuccess { randomEasyMealsList ->
                randomEasyMealsList.forEach { meal ->
                    printFullMeal(meal)
                }
            }.onFailure { exception ->
                println(exception.message)
            }
    }

    // //////////////////////////////////////////////////////////////////////////////////////

    private fun searchMealUsingDate() {
        println("Enter a date to search for meals (format: MM-DD-YYYY):")
        val inputDate = readln()
        println("Loading................")

        searchFoodsUsingDateUseCase(inputDate)
            .onSuccess { meals ->
                displayMealListOfSearchedDate(meals, inputDate)
                fetchMealAccordingID()
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

    private fun fetchMealAccordingID() {
        println("\n If you'd like to view details for a specific meal, enter the Meal ID:")
        val mealId = readln()
        getMealUsingIDUseCase(mealId)
            .onSuccess { meals ->
                meals.forEach { meal -> printFullMeal(meal) }
            }.onFailure { e ->
                println("\n Could not retrieve meal details: ${e.message}")
            }
    }

    // //////////////////////////////////////////////////////////////////////////////////////

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

    // //////////////////////////////////////////////////////////////////////////////////////

    private fun startShowRandomPotatoMeals() {
        println("${ConsoleStyle.BOLD}${ConsoleColors.CYAN}This is very yummy, try this potato meals and Ed3yly.${ConsoleStyle.RESET}")
        getRandomPotatoMealsUseCase()
            .onSuccess { mealsWithPotato ->
                printPotatoMealsResult(mealsWithPotato)
            }.onFailure { exception ->
                println(exception.message)
            }
    }

    private fun printPotatoMealsResult(mealsWithPotato: List<Meal>) {
        if (mealsWithPotato.size < 10) {
            println("Unfortunately we don't have 10 meals right now.\uD83E\uDD7A")
            Thread.sleep(1000)
            println("But cheers up he have: ${mealsWithPotato.size}\uD83D\uDE03")
            Thread.sleep(1500)
            println("So here is ${mealsWithPotato.size} potato meals instead\uD83D\uDE09\n\n")
            Thread.sleep(1000)
        }
        mealsWithPotato.forEach { printMeal(it) }

        getUserPotatoInterestMeal(mealsWithPotato)
    }

    private fun getUserPotatoInterestMeal(mealsWithPotato: List<Meal>) {
        println("Are you interest in any of this meals? (y/n)")
        var userInput = readlnOrNull() ?: ""

        if (userInput == "y") {
            println("Please enter meal id: ")
            userInput = readlnOrNull() ?: ""
            printFullMeal(mealsWithPotato.first { it.id == userInput.toInt() })
            println("Bon-appetit\uD83D\uDE09")
        }
    }

    companion object {
        const val TABLE_PAGE_SIZE = 1000
        const val MEAL_NAME_HEADER = "Meal Name"
        const val MEAL_PROTEIN_HEADER = "Protein (g)"
    }
}
