package org.seoulsquad.logic.useCase

import logic.model.Meal
import logic.utils.InvalidIdException
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.model.MealDate
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.isIdExistingAtList

class GetMealUsingIDUseCase(
    private val repository: MealRepository
) {
    operator fun invoke(id: Int?, meals: List<MealDate>): Result<Meal> {

        return id?.takeIf { it > 0 }?.let {

            meals.isIdExistingAtList(id).takeIf { it }?.let {

                repository.getAllMeals().firstOrNull { it.id == id.toInt() }?.let { Result.success(it) }
                    ?: Result.failure(NoMealsFoundException())

            } ?: Result.failure(NoMealsFoundException())

        } ?: Result.failure(InvalidIdException())
    }

}
