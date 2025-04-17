package logic.useCase

import logic.model.Meal
import org.seoulsquad.logic.repository.MealRepository

class GetMealsByCaloriesAndProtein(
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

        return allMeals.filter { meal ->
            val caloriesInRange = meal.nutrition.calories >= (targetCalories - calorieRange) &&
                    meal.nutrition.calories <= (targetCalories + calorieRange)
            val proteinInRange = meal.nutrition.protein >= (targetProtein - proteinRange) &&
                    meal.nutrition.protein <= (targetProtein + proteinRange)

            caloriesInRange && proteinInRange
        }
    }
}