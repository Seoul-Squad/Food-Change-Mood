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
            "10" -> exploreOtherCountriesFoodConsole.exploreOtherCountriesFood()
            "12" -> showRandomPotatoMealsUi.startShowRandomPotatoMeals()
            "13" -> mealsWithHighCaloriesUi.getMealsWithHighCalories()
            "14" -> seaFoodMealsSortedByProteinConsole.startSeafoodMealsSortedByProtein()
            "15" -> italianLargeMealsConsole.startItalianLargeMealsFlow()
            else -> println("❌ Invalid option. Please try again!")
        }
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
}
