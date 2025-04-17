package org.seoulsquad.presentation

import logic.model.Meal
import org.seoulsquad.logic.useCase.GetSortedSeafoodMealsUseCase
import kotlin.math.max

class SeaFoodMealsSortedByProteinUi(
    private val getSortedSeafoodMealsUseCase: GetSortedSeafoodMealsUseCase,
) {
    fun startSeafoodMealsSortedByProtein() {
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

    companion object {
        const val TABLE_PAGE_SIZE = 1000
        const val MEAL_NAME_HEADER = "Meal Name"
        const val MEAL_PROTEIN_HEADER = "Protein (g)"
    }
}

