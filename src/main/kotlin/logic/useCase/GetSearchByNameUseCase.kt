package org.seoulsquad.logic.useCase

import logic.model.Meal
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.SearchAlgorithm

class GetSearchByNameUseCase(private val mealRepo: MealRepository) {
    fun getSearchByName(query: String, strategy: SearchAlgorithm): Result<List<Meal>> =
        strategy.search(mealRepo.getAllMeals(), query)
}
