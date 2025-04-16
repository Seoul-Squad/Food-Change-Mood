package presentation

import logic.model.Meal
import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.seoulsquad.logic.useCase.GetSortedSeafoodMealsUseCase
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption
import kotlin.math.max

class ConsoleUi(
    private val exploreOtherCountriesFoodUseCase: ExploreOtherCountriesFoodUseCase,
    private val getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase,
    private val getSortedSeafoodMealsUseCase: GetSortedSeafoodMealsUseCase,
) {
    fun start() {
        when (getUserInput()) {
            "6" -> startSweetsWithNoEggsFlow()
            "10" -> exploreOtherCountriesFood()
            "14" -> startSeafoodMealsSortedByProtein()
            else -> println("Invalid option. Please try again.")
        }
    }

    private fun getUserInput(): String = readlnOrNull() ?: ""

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
        val nameHeader = "Name"
        val nameColumnWidth = maxOf(nameHeader.length, meals.maxOf { it.name.length })
        val proteinHeader = "Protein (g)"
        val proteinColumnWidth = max(proteinHeader.length, meals.maxOf { "%.1f".format(it.nutrition.protein).length })
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
            "| ${"#".padEnd(indexColumnWidth, ' ')} | ${"Meal Name".padEnd(nameColumnWidth, ' ')} | ${
                "Protein (g)".padEnd(
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

    companion object {
        const val TABLE_PAGE_SIZE = 1000
    }
}
