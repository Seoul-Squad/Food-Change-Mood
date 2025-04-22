package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.veryLowNutritionValue


class GetHealthyFastFoodUseCase(
    private val mealRepo: MealRepository,
    ) {

    fun getFastHealthyMeals(): Result<List<Meal>> {
        val meals = mealRepo.getAllMeals()

        val nutritionStats = getNutritionWithLowStats(meals , NutritionPrecentage)
        val filteredMeals = meals.filter { isMealHealthyAndFastToPrepare(it, nutritionStats) }
        return filteredMeals
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoMealsFoundException())
    }

    private fun getNutritionWithLowStats(meals: List<Meal>, NutritionPrecentage: Double ): List<Double> {

        val fat = meals.map { it.nutrition.totalFat }.sorted().veryLowNutritionValue(NutritionPrecentage)
        val carbohydrate = meals.map { it.nutrition.carbohydrates }.sorted().veryLowNutritionValue(NutritionPrecentage)
        val saturatedFat = meals.map { it.nutrition.saturatedFat }.sorted().veryLowNutritionValue(NutritionPrecentage)
        return listOf(fat, saturatedFat, carbohydrate)
    }

    private fun isMealHealthyAndFastToPrepare(meal: Meal, lowNutrition: List<Double>): Boolean {
        val (lowFat, lowSaturatedFat, lowCarbohydrate) = lowNutrition
        return meal.preparationTimeInMinutes <= 15 &&
                meal.nutrition.totalFat <= lowFat &&
                meal.nutrition.saturatedFat <= lowSaturatedFat &&
                meal.nutrition.carbohydrates <= lowCarbohydrate
    }

    companion object {
       private  const val NutritionPrecentage = 0.25
    }
}

