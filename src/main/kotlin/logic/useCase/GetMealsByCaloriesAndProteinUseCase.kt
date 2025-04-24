package logic.useCase

import logic.model.Meal
import org.seoulsquad.logic.repository.MealRepository
import logic.utils.NoMealsFoundException

class GetMealsByCaloriesAndProteinUseCase(
    private val mealRepository: MealRepository
) {
    operator fun invoke(
        targetCalories: Double,
        targetProtein: Double,
        tolerancePercent: Int = 10
    ): List<Meal> {
        val (allMeals, calorieRange, proteinRange) = calculateCaloriesAndProteinMargin(
            targetCalories, tolerancePercent, targetProtein
        )

        val filteredMeals = allMeals.filter { meal ->
            isCaloriesAndProteinInRange(meal, targetCalories, targetProtein, calorieRange, proteinRange)
        }

        if (filteredMeals.isEmpty()) {
            throw NoMealsFoundException()
        }
        return filteredMeals
    }

    private fun calculateCaloriesAndProteinMargin(
        targetCalories: Double,
        tolerancePercent: Int,
        targetProtein: Double
    ): Triple<List<Meal>, Double, Double> {
        val allMeals = mealRepository.getAllMeals()
        val calorieMargin = targetCalories * tolerancePercent / 100
        val proteinMargin = targetProtein * tolerancePercent / 100
        return Triple(allMeals, calorieMargin, proteinMargin)
    }

    private fun isCaloriesAndProteinInRange(
        meal: Meal,
        targetCalories: Double,
        targetProtein: Double,
        calorieMargin: Double,
        proteinMargin: Double
    ): Boolean {
        val caloriesInRange = meal.nutrition.calories in (targetCalories - calorieMargin)..(targetCalories + calorieMargin)
        val proteinInRange = meal.nutrition.protein in (targetProtein - proteinMargin)..(targetProtein + proteinMargin)
        return caloriesInRange && proteinInRange
    }
}