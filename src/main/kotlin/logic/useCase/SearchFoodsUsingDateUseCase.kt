package org.seoulsquad.logic.useCase

import kotlinx.datetime.LocalDate
import logic.utils.InvalidDateException
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.model.MealDate
import org.seoulsquad.logic.repository.MealRepository


class SearchFoodsUsingDateUseCase(
    private val repository: MealRepository
) {
    operator fun invoke(date: LocalDate?): Result<List<MealDate>> {
        return date?.let {
            repository.getAllMeals()
                .filter { it.submittedAt == date }
                .takeIf { it.isNotEmpty() }
                ?.map { MealDate(id = it.id, nameOfMeal = it.name, date = it.submittedAt!!) }
                ?.let { Result.success(it) } ?: Result.failure(NoMealsFoundException())
        } ?: Result.failure(InvalidDateException())

    }
}
