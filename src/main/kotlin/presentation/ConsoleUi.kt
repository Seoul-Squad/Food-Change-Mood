package presentation

import logic.model.Meal
import org.seoulsquad.logic.useCase.GetHealthyFastFoodUseCase
import org.seoulsquad.presentation.*

class ConsoleUi(
    private val searchByNameConsole: SearchByNameUi,
    private val iraqiMealsUi: IraqiMealsUi,
    private val randomEasyMealsUi: RandomEasyMealsUi,
    private val guessGameUi: GuessGameUi,
    private val sweetsWithNoEggsConsole: SweetsWithNoEggsUi,
    private val ketoDietMealsUi: KetoDietMealsUi,
    private val searchMealUsingDateUi: SearchMealUsingDateUi,
    private val exploreOtherCountriesFoodConsole: ExploreOtherCountriesFoodUi,
    private val ingredientGameUi: IngredientGameUi,
    private val showRandomPotatoMealsUi: ShowRandomPotatoMealsUi,
    private val mealsWithHighCaloriesUi: MealsWithHighCaloriesUi,
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase,
    private val seaFoodMealsSortedByProteinConsole: SeaFoodMealsSortedByProteinUi,
    private val italianLargeMealsConsole: ItalianLargeMealsUi,
    private val mealsByCaloriesAndProteinUi: MealsByCaloriesAndProteinUi,
) {
    fun start() {
        showWelcomeScreen()
        while (true) {
            printMenu()
            when (getUserInput()) {
                "1" -> presentHealthyMeal()
                "2" -> searchByNameConsole.searchByMealName()
                "3" -> iraqiMealsUi.startIraqiMealsFlow()
                "4" -> randomEasyMealsUi.printRandomEasyMeals()
                "5" -> guessGameUi.startGuessGame()
                "6" -> sweetsWithNoEggsConsole.startSweetsWithNoEggsFlow()
                "7" -> ketoDietMealsUi.startKetoDietFlow()
                "8" -> searchMealUsingDateUi.searchMealUsingDate()
                "9" -> mealsByCaloriesAndProteinUi.startGetMealsByCaloriesAndProtein()
                "10" -> exploreOtherCountriesFoodConsole.exploreOtherCountriesFood()
                "11" -> ingredientGameUi.startIngredientGame()
                "12" -> showRandomPotatoMealsUi.startShowRandomPotatoMeals()
                "13" -> mealsWithHighCaloriesUi.getMealsWithHighCalories()
                "14" -> seaFoodMealsSortedByProteinConsole.startSeafoodMealsSortedByProtein()
                "15" -> italianLargeMealsConsole.startItalianLargeMealsFlow()
                "0" -> return
                else -> println("❌ Invalid option. Please try again!")
            }
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
        println("1  🥦 Get healthy meals")
        println("2  🔍 Search Meal by Name")
        println("3  🍲 Iraqi Meals")
        println("4  🥗 Easy Meals")
        println("5  🎯 Guess the Meal Game")
        println("6  🍰 Sweets Without Eggs")
        println("7  🥓 Keto Diet Meals")
        println("8  📅 Search Meals by Date")
        println("9     Gym Helper ")
        println("10 🌍 Explore Other Countries' Food")
        println("11 🥨 Ingredient Game")
        println("12 🥔 Show 10 Random Potato Meals")
        println("13 🔥 Meals with High Calories")
        println("14 🐟 Seafood Meals Sorted by Protein")
        println("15 🍝 Italian Large Meals")
        println("0  🚶‍♂️ Exit")
        println("------------------------------------------------")
        print("👉 Enter your choice: ")
    }

    private fun getUserInput(): String = readlnOrNull() ?: ""

    private fun presentHealthyMeal() {
        greetingMessageForGetHealthyMealFeature()
        getHealthyFastFoodUseCase
            .getFastHealthyMeals()
            .onSuccess { it.forEach { printHealthyMealsThanCanPreparedUnder15MinutesAndLowNutrition(it) } }
            .onFailure { println(it.message) }
    }

    private fun greetingMessageForGetHealthyMealFeature() {
        println("Hello this is your list of healthy fast food that can be prepared in under 15 with Low nutrition's")
    }

    private fun printHealthyMealsThanCanPreparedUnder15MinutesAndLowNutrition(meal: Meal) {
        with(meal) {
            println("Meal: $name")
            println("Time to Prepare: $minutes minutes")
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
}
