package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.KnuthMorrisPrattMealSearchAlgorithm

class SearchMealsByNameUseCase(private val mealRepo: MealRepository) {
    operator fun invoke(query: String): Result<List<Meal>> {
        if (query.isBlank()) return Result.failure(NoMealsFoundException("No meals found"))
        return KnuthMorrisPrattMealSearchAlgorithm().searchMeal(mealRepo.getAllMeals(), query)
    }
}
