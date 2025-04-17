package presentation

import org.seoulsquad.presentation.*

class ConsoleUi(
    private val exploreOtherCountriesFoodConsole: ExploreOtherCountriesFoodUi,
    private val sweetsWithNoEggsConsole: SweetsWithNoEggsUi,
    private val ketoDietMealsUi: KetoDietMealsUi,
    private val searchMealUsingDateUi: SearchMealUsingDateUi,
    private val searchByNameConsole: SearchByNameUi,
    private val iraqiMealsUi: IraqiMealsUi,
    private val mealsWithHighCaloriesUi: MealsWithHighCaloriesUi,
    private val guessGameUi: GuessGameUi,
    private val randomEasyMealsUi: RandomEasyMealsUi,
    private val italianLargeMealsConsole: ItalianLargeMealsUi,
    private val showRandomPotatoMealsUi: ShowRandomPotatoMealsUi,
    private val seaFoodMealsSortedByProteinConsole: SeaFoodMealsSortedByProteinUi
) {
    fun start() {
        showWelcomeScreen()
        printMenu()
        when (getUserInput()) {
            "2" -> searchByNameConsole.searchByMealName()
            "3" -> iraqiMealsUi.startIraqiMealsFlow()
            "4" -> randomEasyMealsUi.printRandomEasyMeals()
            "5" -> guessGameUi.startGuessGame()
            "6" -> sweetsWithNoEggsConsole.startSweetsWithNoEggsFlow()
            "7" -> ketoDietMealsUi.startKetoDietFlow()
            "8" -> searchMealUsingDateUi.searchMealUsingDate()
            "9" -> startGetMealsByCaloriesAndProtein()
            "10" -> exploreOtherCountriesFoodConsole.exploreOtherCountriesFood()
            "12" -> showRandomPotatoMealsUi.startShowRandomPotatoMeals()
            "13" -> mealsWithHighCaloriesUi.getMealsWithHighCalories()
            "14" -> seaFoodMealsSortedByProteinConsole.startSeafoodMealsSortedByProtein()
            "15" -> italianLargeMealsConsole.startItalianLargeMealsFlow()
            else -> println("❌ Invalid option. Please try again!")
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

    private fun showWelcomeScreen() {
        println("╔═════════════════════════════════════════╗")
        println("║ 🍽️ Welcome to Meal Explorer Terminal 🍽️ ║")
        println("╚═════════════════════════════════════════╝")
        println("✨ Discover meals from around the world, explore diets, and enjoy a tasty adventure!\n")
    }

    private fun printMenu() {
        println("🌟 Choose a task by entering the number:")
        println("------------------------------------------------")
        println("2  🔍 Search Meal by Name")
        println("3  🍲 Iraqi Meals")
        println("4  🥗 Easy Meals")
        println("5  🎯 Guess the Meal Game")
        println("6  🍰 Sweets Without Eggs")
        println("7  🥓 Keto Diet Meals")
        println("8  📅 Search Meals by Date")
        println("10 🌍 Explore Other Countries' Food")
        println("12 🥔 Show 10 Random Potato Meals")
        println("13 🔥 Meals with High Calories")
        println("14 🐟 Seafood Meals Sorted by Protein")
        println("15 🍝 Italian Large Meals")
        println("------------------------------------------------")
        print("👉 Enter your choice: ")
    }

    private fun getUserInput(): String = readlnOrNull() ?: ""
    fun startGetMealsByCaloriesAndProtein() {
        println("=== Meal Nutrition Filter ===")
//        val allMeals = getAllMealsUseCase()  // Call the GetAllMealsUseCase
//
//        println("Total meals: ${allMeals.size}")
        print("Enter target calories: ")
        val targetCalories = readLine()?.toIntOrNull() ?: 0

        print("Enter target protein (g): ")
        val targetProtein = readLine()?.toIntOrNull() ?: 0

        // Execute use case with user input
        val meals = getMealsByCaloriesAndProtein.execute(
            targetCalories = targetCalories,
            targetProtein = targetProtein,
        )
        println("The available meals with ~${targetCalories}g calories and ~${targetProtein}g protein are: ${meals.size}")
        meals.forEach { println("- ${it.name}") }
    }


}
