package org.seoulsquad.logic.useCase

import logic.model.Meal
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.percentage


class GetHealthyFastFoodUseCase(
    private val mealRepo: MealRepository,
    ) {

    fun getFastHealthyMeals(): Result<List<Meal>> {
        val meals = mealRepo.getAllMeals()

        val nutritionStats = getNutritionWithLowStats(meals)
        val filteredMeals = meals.filter { isMealHealthyAndFastToPrepare(it, nutritionStats) }
        return filteredMeals
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No Healthy meal that can be prepared under 15 minutes"))
    }

    private fun getNutritionWithLowStats(meals: List<Meal>, percentage: Double = 0.25): List<Double> {

        val fat = meals.map { it.nutrition.totalFat }.percentage(percentage)
        val carbohydrate = meals.map { it.nutrition.carbohydrates }.percentage(percentage)
        val saturatedFat = meals.map { it.nutrition.saturatedFat }.percentage(percentage)
        return listOf(fat, saturatedFat, carbohydrate)
    }

    private fun isMealHealthyAndFastToPrepare(meal: Meal, lowNutrition: List<Double>): Boolean {
        val (lowFat, lowSaturatedFat, lowCarbohydrate) = lowNutrition
        return meal.minutes <= 15 &&
                meal.nutrition.totalFat <= lowFat &&
                meal.nutrition.saturatedFat <= lowSaturatedFat &&
                meal.nutrition.carbohydrates <= lowCarbohydrate
    }
}

