package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetSeafoodMealsSortedByProteinUseCase
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.TablePrinter

class SeaFoodMealsSortedByProteinUi(
    private val getSortedSeafoodMealsUseCase: GetSeafoodMealsSortedByProteinUseCase,
    private val viewer: Viewer,
    private val tablePrinter: TablePrinter
) {
    fun startSeafoodMealsSortedByProtein() {
        viewer.display("Loading, Please wait...")
        IntRange
        getSortedSeafoodMealsUseCase().onSuccess { meals ->
            tablePrinter.printTable(
                headers = headers,
                columnValues = listOf(
                    (1..meals.size).map { it.toString() },
                    meals.map { it.name },
                    meals.map { it.nutrition.protein.toString() }
                )
            )
        }.onFailure { e ->
            viewer.display(e.message)
        }
    }

    companion object {
        val headers = listOf("#", "Meal Name", "Protein (g)")
    }
}

