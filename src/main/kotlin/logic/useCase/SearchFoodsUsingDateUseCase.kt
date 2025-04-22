package org.seoulsquad.logic.useCase

import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.model.MealDate
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.convertDateToLocalDate
import org.seoulsquad.logic.utils.paresDate


class SearchFoodsUsingDateUseCase(
    private val repository: MealRepository
) {
    operator fun invoke(data: String): Result<List<MealDate>> {
        val date = convertDateToLocalDate(paresDate(data).getOrElse { return Result.failure(it) })
        return repository.getAllMeals()
            .filter { it.submittedAt == date }
            .takeIf { it.isNotEmpty() }
            ?.map { MealDate(id = it.id, nameOfMeal = it.name, date = it.submittedAt!!) }
            ?.let { Result.success(it) } ?: Result.failure(NoMealsFoundException())
    }
}