package logic.useCase

import logic.model.Meal
import org.seoulsquad.logic.repository.MealRepository
import logic.utils.NoMealsFoundException

class GetMealsByCaloriesAndProteinUseCase(
    private val mealRepository: MealRepository
) {
    fun getMealsByCaloriesAndProtein(
        targetCalories: Int,
        targetProtein: Int,
        tolerancePercent: Int = 10
    ): List<Meal> {
        val (allMeals, calorieRange, proteinRange) = calculateCaloriesAndProteinMargin(
            targetCalories, tolerancePercent, targetProtein
        )

        val filteredMeals = allMeals.filter { meal ->
            isCaloriesAndProteinInRange(meal, targetCalories, targetProtein, calorieRange, proteinRange)
        }

        if (filteredMeals.isEmpty()) {
            throw NoMealsFoundException("No meals match your nutrition criteria.")
        }
        return filteredMeals
    }

    private fun calculateCaloriesAndProteinMargin(
        targetCalories: Int,
        tolerancePercent: Int,
        targetProtein: Int
    ): Triple<List<Meal>, Int, Int> {
        val allMeals = mealRepository.getAllMeals()
        val calorieMargin = targetCalories * tolerancePercent / 100
        val proteinMargin = targetProtein * tolerancePercent / 100
        return Triple(allMeals, calorieMargin, proteinMargin)
    }

    private fun isCaloriesAndProteinInRange(
        meal: Meal,
        targetCalories: Int,
        targetProtein: Int,
        calorieMargin: Int,
        proteinMargin: Int
    ): Boolean {
        val caloriesInRange = meal.nutrition.calories >= (targetCalories - calorieMargin) &&
                meal.nutrition.calories <= (targetCalories + calorieMargin)
        val proteinInRange = meal.nutrition.protein >= (targetProtein - proteinMargin) &&
                meal.nutrition.protein <= (targetProtein + proteinMargin)
        return caloriesInRange && proteinInRange
    }
}