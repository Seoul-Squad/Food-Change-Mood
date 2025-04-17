package presentation

import org.seoulsquad.presentation.ExploreOtherCountriesFoodUi
import org.seoulsquad.presentation.GuessGameUi
import org.seoulsquad.presentation.IngredientGame
import org.seoulsquad.presentation.IraqiMealsUi
import org.seoulsquad.presentation.ItalianLargeMealsUi
import org.seoulsquad.presentation.KetoDietMealsUi
import org.seoulsquad.presentation.MealsWithHighCaloriesUi
import org.seoulsquad.presentation.RandomEasyMealsUi
import org.seoulsquad.presentation.SeaFoodMealsSortedByProteinUi
import org.seoulsquad.presentation.SearchByNameUi
import org.seoulsquad.presentation.SearchMealUsingDateUi
import org.seoulsquad.presentation.ShowRandomPotatoMealsUi
import org.seoulsquad.presentation.SweetsWithNoEggsUi

class ConsoleUi(
    private val searchByNameConsole: SearchByNameUi,
    private val iraqiMealsUi: IraqiMealsUi,
    private val randomEasyMealsUi: RandomEasyMealsUi,
    private val guessGameUi: GuessGameUi,
    private val sweetsWithNoEggsConsole: SweetsWithNoEggsUi,
    private val ketoDietMealsUi: KetoDietMealsUi,
    private val searchMealUsingDateUi: SearchMealUsingDateUi,
    private val exploreOtherCountriesFoodConsole: ExploreOtherCountriesFoodUi,
    private val ingredientGame: IngredientGame,
    private val showRandomPotatoMealsUi: ShowRandomPotatoMealsUi,
    private val mealsWithHighCaloriesUi: MealsWithHighCaloriesUi,
    private val seaFoodMealsSortedByProteinConsole: SeaFoodMealsSortedByProteinUi,
    private val italianLargeMealsConsole: ItalianLargeMealsUi,
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
            "11" -> ingredientGame.startIngredientGame()
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
        println("11. Ingredient Game")
        println("12. Show 10 random meals contains potato")
        println("13. Meals with high calories")
        println("14. Seafood meals sorted by protein content")
        println("15. Italian Large Meals")
    }

    private fun getUserInput(): String = readlnOrNull() ?: ""
}
