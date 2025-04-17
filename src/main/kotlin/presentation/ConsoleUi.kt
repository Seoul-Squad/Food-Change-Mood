package presentation

import org.seoulsquad.presentation.*

class ConsoleUi(
    private val exploreOtherCountriesFoodConsole:ExploreOtherCountriesFoodUi,
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
        println("12. Show 10 random meals contains potato")
        println("13. Meals with high calories")
        println("14. Seafood meals sorted by protein content")
        println("15. Italian Large Meals")
    }

    private fun getUserInput(): String = readlnOrNull() ?: ""

}
