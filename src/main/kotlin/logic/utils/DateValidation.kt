package org.seoulsquad.logic.utils

import kotlinx.datetime.*
import logic.utils.Constants


fun String.toLocalDate(): LocalDate? {
    return split(Regex("[/-]"))
        .takeIf { checkDateValidation(it) }
        ?.let { convertDateToLocalDate(it) }
}

private fun convertDateToLocalDate(date: List<String>): LocalDate {
    return LocalDate(
        month = Month.of(date[Constants.MONTH].toInt()),
        dayOfMonth = date[Constants.DAY].toInt(),
        year = date[Constants.YEAR].toInt(),
    )

}

private fun checkDateValidation(partsOfDate: List<String>) =
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

private fun checkIsDateFuture(localDate: LocalDate): Boolean {
    return LocalDate(
        month = Month.of(localDate.monthNumber),
        dayOfMonth = localDate.dayOfMonth,
        year = localDate.year
    ) <= Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

}