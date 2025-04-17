package org.seoulsquad.logic.useCase

import logic.model.Meal
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.precntage


class GetHealthyFastFoodUseCase(
    private val mealRepo: MealRepository,

    ) {


    fun getFastHealthyMeals(): Result<List<Meal>> {
        val meals = mealRepo.getAllMeals()

   val  nutritionStats = getNutrtionWithLowStats(meals)
        val filteredMeals = meals.filter { isMealHealthyAndFastToPrepare(it, nutritionStats ) }
        return filteredMeals
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No Healthy meal that can be prepared under 15 minutes"))
    }

    private fun getNutrtionWithLowStats(meals: List<Meal> , veryLow: Double= 0.25): List<Double> {

        val fat = meals.map { it.nutrition.totalFat }.precntage(veryLow)
        val carbohydrate = meals.map { it.nutrition.carbohydrates }.precntage(veryLow)
        val saturatedFat = meals.map { it.nutrition.saturatedFat }.precntage(veryLow)
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

