package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.repository.MealRepository
import logic.utils.InvalidId
import logic.utils.InvalidSearchException

class GetMealUsingIDUseCase(
    private val repository: MealRepository
) {
    operator fun invoke(id: String): Result<List<Meal>> {
        val isIdValid =checkIDValidation(id).getOrElse { return Result.failure(InvalidId()) }
        return repository.getAllMeals()
            .takeIf { isIdValid }
            ?.filter { it.id == id.toInt() }
            ?.takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) } ?: Result.failure(InvalidSearchException())
    }

    private fun checkIDValidation(id: String): Result<Boolean> {
        return Result.success(id.toIntOrNull() != null && id.toInt() > 0)
    }
}