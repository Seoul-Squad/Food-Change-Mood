package org.seoulsquad.data.repository

import data.utils.MealCsvFileReader
import data.utils.MealCsvParser
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.model.Meal

class MealRepositoryImpl(
    private val fileReader: CsvFileReader,
    private val parser: MealCsvParser,
) : MealRepository {
    private val allMeals: List<Meal>

    init {
        allMeals = loadMeals()
    }

    override fun getAllMeals(): List<Meal> = allMeals

    private fun loadMeals(): List<Meal> {
        val csvData = fileReader.readCsv()
        val headers = csvData.headers
        val dataRows = csvData.rows

        if (headers.isEmpty() || dataRows.isEmpty()) return emptyList()

        val meals =
            dataRows.mapNotNull { row ->
                try {
                    parser.parse(headers, row)
                } catch (e: Exception) {
                    null
                }
            }

        return meals
    }

}