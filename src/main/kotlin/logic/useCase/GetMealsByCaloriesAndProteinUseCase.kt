package logic.useCase

import logic.model.Meal
import org.seoulsquad.logic.repository.MealRepository
import logic.utils.NoMealsFoundException

class GetMealsByCaloriesAndProteinUseCase(
    private val mealRepository: MealRepository
) {
    fun execute(
        targetCalories: Int,
        targetProtein: Int,
        tolerancePercent: Int = 10
    ): List<Meal> {
        val allMeals = mealRepository.getAllMeals()
        val calorieRange = targetCalories * tolerancePercent / 100
        val proteinRange = targetProtein * tolerancePercent / 100

        val filteredMeals = allMeals.filter { meal ->
            isMealWithinRange(meal, targetCalories, targetProtein, calorieRange, proteinRange)
        }

        if (filteredMeals.isEmpty()) {
            throw NoMealsFoundException("No meals match your nutrition criteria.")
        }
        return filteredMeals
    }

    private fun isMealWithinRange(
        meal: Meal,
        targetCalories: Int,
        targetProtein: Int,
        calorieRange: Int,
        proteinRange: Int
    ): Boolean {
        val caloriesInRange = meal.nutrition.calories >= (targetCalories - calorieRange) &&
                meal.nutrition.calories <= (targetCalories + calorieRange)
        val proteinInRange = meal.nutrition.protein >= (targetProtein - proteinRange) &&
                meal.nutrition.protein <= (targetProtein + proteinRange)
        return caloriesInRange && proteinInRange
    }
}