package org.seoulsquad.logic.use_case

import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.model.Meal

class GetKetoDietMealUseCase(
    private val mealRepo: MealRepository,
) {
    fun getKetoDietMeal(): Result<List<Meal>> =
        mealRepo
            .getAllMeals()
            .filter (::isKetoDietMeal)
            .let {
                if (it.isNotEmpty()) Result.success(it)
                else Result.failure(NoSuchElementException("No Keto Diet Meals found"))
            }


    private fun isKetoDietMeal(meal: Meal): Boolean{
        val nutrition = meal.nutrition
        return nutrition.carbohydrates < 20 &&
                nutrition.totalFat > nutrition.protein &&
                nutrition.sugar < 5
    }
}