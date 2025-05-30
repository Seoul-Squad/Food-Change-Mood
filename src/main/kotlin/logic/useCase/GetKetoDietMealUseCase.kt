package logic.useCase

import logic.utils.Constants.NutritionConstants
import logic.model.Meal
import logic.model.Nutrition
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GetKetoDietMealUseCase(
    private val mealRepo: MealRepository,
) {
    fun getKetoDietMeal(): Result<List<Meal>> =
        mealRepo
            .getAllMeals()
            .filter(::isKetoDietMeal)
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoMealsFoundException())

    private fun isKetoDietMeal(meal: Meal): Boolean {
        val nutrition = meal.nutrition
        val netCarbs = nutrition.carbohydrates
        val sugar = nutrition.sugar

        val (fatCalories, proteinCalories, carbCalories) = calculateTotalCalories(nutrition)
        val (fatPercent, proteinPercent, carbPercent) = calculateMacrosPercentages(fatCalories, proteinCalories, carbCalories)

        return validateKetoConstraints(
            fatPercent, proteinPercent, carbPercent, netCarbs, sugar
        )
    }

    private fun calculateTotalCalories(nutrition: Nutrition): Triple<Double, Double, Double> {
        val fatCalories = nutrition.totalFat * NutritionConstants.CALORIES_PER_GRAM_FAT
        val proteinCalories = nutrition.protein * NutritionConstants.CALORIES_PER_GRAM_PROTEIN
        val carbCalories = nutrition.carbohydrates * NutritionConstants.CALORIES_PER_GRAM_CARBS
        return Triple(fatCalories, proteinCalories, carbCalories)
    }

    private fun calculateMacrosPercentages(
        fatCalories: Double,
        proteinCalories: Double,
        carbCalories: Double
    ): Triple<Double, Double, Double> {
        val totalCalories = fatCalories + proteinCalories + carbCalories
        if (totalCalories <= 0.0) return Triple(-1.0, -1.0, -1.0)
        val fatPercent = (fatCalories / totalCalories) * 100
        val proteinPercent = (proteinCalories / totalCalories) * 100
        val carbPercent = (carbCalories / totalCalories) * 100
        return Triple(fatPercent, proteinPercent, carbPercent)
    }

    fun validateKetoConstraints(
        fatPercent: Double,
        proteinPercent: Double,
        carbPercent: Double,
        netCarbs: Double,
        sugar: Double
    ): Boolean {
        return fatPercent in NutritionConstants.FAT_PERCENT_MIN..NutritionConstants.FAT_PERCENT_MAX &&
                proteinPercent in NutritionConstants.PROTEIN_PERCENT_MIN..NutritionConstants.PROTEIN_PERCENT_MAX &&
                carbPercent in NutritionConstants.CARB_PERCENT_MIN..NutritionConstants.CARB_PERCENT_MAX &&
                netCarbs < NutritionConstants.MAX_NET_CARBS &&
                sugar < NutritionConstants.MAX_SUGAR
    }
}
