package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.InvalidIdException
import logic.utils.InvalidSearchException
import org.seoulsquad.logic.repository.MealRepository

class GetMealUsingIDUseCase(
    private val repository: MealRepository
) {
    operator fun invoke(id: String): Result<List<Meal>> {
        val isIdValid = checkIDValidation(id).getOrElse { return Result.failure(InvalidIdException()) }
        return repository.getAllMeals()
            .takeIf { isIdValid }
            ?.filter { it.id == id.toInt() }
            ?.takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) } ?: Result.failure(InvalidSearchException())
    }

    private fun checkIDValidation(id: String): Result<Boolean> {
        return id.takeIf { it.toIntOrNull() != null && it.toInt() > 0 }?.let { Result.success(true) } ?: Result.failure(
            InvalidIdException()
        )
    }
}