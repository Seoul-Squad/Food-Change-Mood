package data.utils

import data.utils.Constants.ColumnIndex.CALORIES
import data.utils.Constants.ColumnIndex.CARBOHYDRATES
import data.utils.Constants.ColumnIndex.PROTEIN
import data.utils.Constants.ColumnIndex.SATURATED_FAT
import data.utils.Constants.ColumnIndex.SODIUM
import data.utils.Constants.ColumnIndex.SUGAR
import data.utils.Constants.ColumnIndex.TOTAL_FAT
import data.utils.Constants.ColumnName.COLUMN_CONTRIBUTOR_ID
import data.utils.Constants.ColumnName.COLUMN_DESCRIPTION
import data.utils.Constants.ColumnName.COLUMN_ID
import data.utils.Constants.ColumnName.COLUMN_INGREDIENTS
import data.utils.Constants.ColumnName.COLUMN_MINUTES
import data.utils.Constants.ColumnName.COLUMN_NAME
import data.utils.Constants.ColumnName.COLUMN_N_INGREDIENTS
import data.utils.Constants.ColumnName.COLUMN_N_STEPS
import data.utils.Constants.ColumnName.COLUMN_STEPS
import data.utils.Constants.ColumnName.COLUMN_SUBMITTED
import data.utils.Constants.ColumnName.COLUMN_TAGS
import data.utils.Constants.NUTRITION
import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.model.Nutrition


class MealCsvParser {

    fun parse(headers: List<String>, values: List<String>): Meal {
        val fields = headers.zip(values).toMap()
        val nutrition = parseNumberArrayString(fields[NUTRITION] ?: "[]")

        return Meal(
            id = fields[COLUMN_ID]?.toIntOrNull() ?: 0,
            name = fields[COLUMN_NAME] ?: "",
            preparationTimeInMinutes = fields[COLUMN_MINUTES]?.toIntOrNull() ?: 0,
            contributorId = fields[COLUMN_CONTRIBUTOR_ID]?.toIntOrNull() ?: 0,
            submittedAt = parseDate(fields[COLUMN_SUBMITTED]),
            tags = parseArrayString(fields[COLUMN_TAGS] ?: "[]"),
            nutrition = Nutrition(
                calories = nutrition.getOrElse(CALORIES) { 0.0 },
                totalFat = nutrition.getOrElse(TOTAL_FAT) { 0.0 },
                sugar = nutrition.getOrElse(SUGAR) { 0.0 },
                sodium = nutrition.getOrElse(SODIUM) { 0.0 },
                protein = nutrition.getOrElse(PROTEIN) { 0.0 },
                saturatedFat = nutrition.getOrElse(SATURATED_FAT) { 0.0 },
                carbohydrates = nutrition.getOrElse(CARBOHYDRATES) { 0.0 }
            ),
            numberOfSteps = fields[COLUMN_N_STEPS]?.toIntOrNull() ?: 0,
            steps = parseArrayString(fields[COLUMN_STEPS] ?: "[]"),
            description = fields[COLUMN_DESCRIPTION]?.takeIf { it.isNotBlank() },
            ingredients = parseArrayString(fields[COLUMN_INGREDIENTS] ?: "[]"),
            numberOfIngredients = fields[COLUMN_N_INGREDIENTS]?.toIntOrNull() ?: 0
        )

    }


    private fun parseDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    private fun parseArrayString(arrayStr: String): List<String> {
        if (arrayStr.isBlank() || arrayStr == "[]") return emptyList()

        return arrayStr
            .removeSurrounding("[", "]")
            .splitToSequence(',')
            .map { it.trim().removeSurrounding("'").removeSurrounding("\"") }
            .filter { it.isNotBlank() }
            .toList()
    }

    private fun parseNumberArrayString(arrayStr: String): List<Double> {
        if (arrayStr.isBlank() || arrayStr == "[]") return emptyList()

        return arrayStr
            .removeSurrounding("[", "]")
            .split(',')
            .mapNotNull { it.trim().toDoubleOrNull() }
    }

}