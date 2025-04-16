package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.repository.MealRepository


class GetHealthyFastFood(
    private val mealRepo: MealRepository,
) {

    private val veryLow: Double = 0.25
    private lateinit var lowNutritionThresholds: List<Double>

    fun getFastHealthyMeals(): Result<List<Meal>> {
        val meals = mealRepo.getAllMeals()
        lowNutritionThresholds = getNutrtionWithLowStats(meals)

        val filteredMeals = meals.filter(::isMealHealthyAndFastToPrepare)
        return filteredMeals
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No Healthy meal that can be prepared under 15 minutes"))
    }

    fun getNutrtionWithLowStats(meals: List<Meal>): List<Double> {
        fun List<Double>.precntage(p: Double): Double {
            if (isEmpty()) return 0.0
            val sorted = this.sorted()
            val index = (p * (sorted.size - 1)).toInt()
            return sorted[index]
        }

        val fat = meals.map { it.nutrition.totalFat }.precntage(veryLow)
        val carbohydrate = meals.map { it.nutrition.carbohydrates }.precntage(veryLow)
        val saturatedFat = meals.map { it.nutrition.saturatedFat }.precntage(veryLow)

        return listOf(fat, saturatedFat, carbohydrate)
    }

    private fun isMealHealthyAndFastToPrepare(meal: Meal): Boolean {
        val (lowFat, lowSaturatedFat, lowCarbohydrate) = lowNutritionThresholds
        return meal.minutes <= 15 &&
                meal.nutrition.totalFat <= lowFat &&
                meal.nutrition.saturatedFat <= lowSaturatedFat &&
                meal.nutrition.carbohydrates <= lowCarbohydrate
    }
}