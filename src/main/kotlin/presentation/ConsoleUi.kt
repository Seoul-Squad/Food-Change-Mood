package presentation

import logic.utils.Constants.DEFAULT_INGREDIENT
import logic.utils.Constants.MAX_MEALS
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.di.PresentationDependencies

class ConsoleUi(
    private val di: PresentationDependencies,
    private val viewer: Viewer,
    private val reader: Reader,
) {
    fun start() {
        showWelcomeScreen()
        while (true) {
            printMenu()
            if (handleUserInput(reader.readString())) return
        }
    }

    private fun handleUserInput(input: String?): Boolean {
        when (input) {
            "1" -> di.healthyMealUi.presentHealthyMeal()
            "2" -> di.searchByNameConsole.searchMealByName()
            "3" -> di.iraqiMealsUi.startIraqiMealsFlow()
            "4" -> di.randomEasyMealsUi.printRandomEasyMeals()
            "5" -> di.guessMealPreparationTimeGameUI.startGuessGame()
            "6" -> di.sweetsWithNoEggsConsole.startSweetsWithNoEggsFlow()
            "7" -> di.ketoDietMealsUi.startKetoDietFlow()
            "8" -> di.searchMealUsingDateUi.searchMealUsingDate()
            "9" -> di.mealsByCaloriesAndProteinUi.startGetMealsByCaloriesAndProtein()
            "10" -> di.exploreOtherCountriesFoodConsole.exploreOtherCountriesFood()
            "11" -> di.ingredientGameUi.startIngredientGame()
            "12" -> di.showRandomMealsByIngredientUi.startShowRandomMealsByIngredient()
            "13" -> di.mealsWithHighCaloriesUi.getMealsWithHighCalories()
            "14" -> di.seaFoodMealsSortedByProteinConsole.startSeafoodMealsSortedByProtein()
            "15" -> di.italianLargeMealsConsole.startItalianLargeMealsFlow()
            "0" -> return true
            else -> viewer.display("❌ Invalid option. Please try again!")
        }
        return false
    }

    private fun showWelcomeScreen() {
        viewer.display(" ╔═════════════════════════════════════════╗")
        viewer.display(" * 🍽️ Welcome to Meal Explorer Terminal 🍽️ *")
        viewer.display(" ╚═════════════════════════════════════════╝")
        viewer.display("✨ Discover meals from around the world, explore diets, and enjoy a tasty adventure!\n")
    }

    private fun printMenu() {
        viewer.display("🌟 Choose a task by entering the number:")
        viewer.display("------------------------------------------------")
        viewer.display("1  🥦 Show healthy meals")
        viewer.display("2  🔍 Search Meal by Name")
        viewer.display("3  🍲 Show Iraqi Meals")
        viewer.display("4  🥗 Show Easy Meals")
        viewer.display("5  🎯 Guess the Meal Game")
        viewer.display("6  🍰 Show Sweets Without Eggs")
        viewer.display("7  🥓 Show Keto Diet Meals")
        viewer.display("8  📅 Search Meals by Date")
        viewer.display("9  💪 Gym Helper ")
        viewer.display("10 🌍 Explore Other Countries 'Food'")
        viewer.display("11 🥨 Ingredient Game")
        viewer.display("12 🥔 Show $MAX_MEALS Random $DEFAULT_INGREDIENT Meals")
        viewer.display("13 🔥 Meals with High Calories")
        viewer.display("14 🐟 Seafood Meals Sorted by Protein")
        viewer.display("15 🍝 Italian Large Meals")
        viewer.display("0  🚶‍♂️ Exit")
        viewer.display("------------------------------------------------")
        print("👉 Enter your choice: ")
    }
}
