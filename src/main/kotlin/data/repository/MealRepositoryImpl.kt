package org.seoulsquad.data.repository

import data.utils.MealCsvFileReader
import data.utils.MealCsvParser
import org.seoulsquad.logic.repository.MealRepository
import data.model.Meal

class MealRepositoryImpl(
    private val fileReader: MealCsvFileReader,
    private val parser: MealCsvParser,
) : MealRepository {
    override fun getAllMeals(): List<Meal> {
        val csvData = fileReader.readCsv()
        val headers = csvData.headers
        val dataRows = csvData.rows

        if (headers.isEmpty() || dataRows.isEmpty()) return emptyList()

        val meals = dataRows.mapNotNull { row ->
            try { parser.parse(headers, row)} catch (e: Exception) { null }
        }

        return meals
    }

}