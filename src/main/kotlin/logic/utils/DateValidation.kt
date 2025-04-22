package org.seoulsquad.logic.utils

import kotlinx.datetime.*
import logic.utils.Constants
import logic.utils.InvalidDateException

fun paresDate(data: String): Result<List<String>> {
    return data.split(Regex("[/-]"))
        .takeIf { checkDateValidation(it) }
        ?.let { Result.success(it) }
        ?: Result.failure(InvalidDateException())
}

fun convertDateToLocalDate(date: List<String>): LocalDate {
    return LocalDate(
        month = Month.of(date[Constants.MONTH].toInt()),
        dayOfMonth = date[Constants.DAY].toInt(),
        year = date[Constants.YEAR].toInt(),
    )

}

fun checkDateValidation(partsOfDate: List<String>) =
    partsOfDate.all {
        partsOfDate.size == Constants.LENGTH_OF_DATE &&
                partsOfDate[Constants.YEAR].toIntOrNull() != null &&
                partsOfDate[Constants.YEAR].length == 4 &&
                partsOfDate[Constants.MONTH].toIntOrNull() in 1..12 &&
                partsOfDate[Constants.DAY].toIntOrNull() in 1..31 &&
                checkIsDateFuture(
                    LocalDate(
                        monthNumber = partsOfDate[Constants.MONTH].toInt(),
                        dayOfMonth = partsOfDate[Constants.DAY].toInt(),
                        year = partsOfDate[Constants.YEAR].toInt(),
                    )
                )
    }

fun checkIsDateFuture(localDate: LocalDate): Boolean {
    return LocalDate(
        month = Month.of(localDate.monthNumber),
        dayOfMonth = localDate.dayOfMonth,
        year = localDate.year
    ) <= Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

}