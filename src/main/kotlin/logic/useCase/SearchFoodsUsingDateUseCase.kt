package org.seoulsquad.logic.useCase

import kotlinx.datetime.*
import logic.utils.Constants
import logic.utils.InvalidDateForSearchException
import logic.utils.InvalidSearchException
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.model.MealDate


class SearchFoodsUsingDateUseCase(
    private val repository: MealRepository
) {
    operator fun invoke(data: String): Result<List<MealDate>> {
        val date = convertDateToLocalDate(paresDate(data).getOrElse { return Result.failure(it) })
        return repository.getAllMeals()
            .filter { it.submittedAt == date }
            .takeIf { it.isNotEmpty() }
            ?.map { MealDate(id = it.id, nameOfMeal = it.name, date = it.submittedAt!!) }
            ?.let { Result.success(it) } ?: Result.failure(InvalidSearchException())
    }


    private fun paresDate(data: String): Result<List<String>> {
        return splitDate(data)
            .takeIf { checkDateValidation(it) }
            ?.let { Result.success(it) }
            ?: Result.failure(InvalidDateForSearchException())
    }

    private fun convertDateToLocalDate(date: List<String>): LocalDate {
        return LocalDate(
            month = Month.of(date[Constants.MONTH].toInt()),
            dayOfMonth = date[Constants.DAY].toInt(),
            year = date[Constants.YEAR].toInt(),
        )

    }

    private fun splitDate(date: String): List<String> {
        return date.split(Regex("[/-]"))
    }

    private fun checkDateValidation(partsOfDate: List<String>) =
        partsOfDate.all {
            partsOfDate.size == Constants.LENGTH_OF_DATE &&
                    partsOfDate[Constants.YEAR].toIntOrNull() != null &&
                    partsOfDate[Constants.YEAR].length == 4 &&
                    partsOfDate[Constants.MONTH].toIntOrNull() in 1..12 &&
                    partsOfDate[Constants.DAY].toIntOrNull() in 1..31 &&
                    checkIsDateFuture(
                        month = partsOfDate[Constants.MONTH].toInt(),
                        day = partsOfDate[Constants.DAY].toInt(),
                        year = partsOfDate[Constants.YEAR].toInt(),
                    )
        }

    private fun checkIsDateFuture(year: Int, month: Int, day: Int): Boolean {
        return LocalDate(month =  Month.of(month), dayOfMonth=day, year=year) <= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    }
}