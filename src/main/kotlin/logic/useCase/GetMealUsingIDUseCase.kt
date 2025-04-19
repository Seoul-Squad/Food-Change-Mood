package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.InvalidIdException
import logic.utils.InvalidSearchException
import org.seoulsquad.logic.model.MealDate
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.isIdExistingAtList

class GetMealUsingIDUseCase(
    private val repository: MealRepository
) {
    operator fun invoke(id: String, meals: List<MealDate>): Result<Meal> {
        val isIdValid = checkIDValidation(id).getOrElse { return Result.failure(InvalidIdException()) }
        val idExistingAtList = meals.isIdExistingAtList(id)
        return if (isIdValid && idExistingAtList) {
            repository.getAllMeals()
                .firstOrNull { it.id == id.toInt() }
                ?.let { Result.success(it) } ?: Result.failure(InvalidSearchException())
        } else
            return Result.failure(InvalidIdException())
    }

    private fun checkIDValidation(id: String): Result<Boolean> {
        return id.takeIf { it.toIntOrNull() != null && it.toInt() > 0 }?.let { Result.success(true) } ?: Result.failure(
            InvalidIdException()
        )
    }
}