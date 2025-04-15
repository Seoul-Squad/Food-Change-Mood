package data.utils

import kotlinx.datetime.LocalDate
import org.seoulsquad.data.utils.Constants.ColumnIndex.CALORIES
import org.seoulsquad.data.utils.Constants.ColumnIndex.CARBOHYDRATES
import org.seoulsquad.data.utils.Constants.ColumnIndex.PROTEIN
import org.seoulsquad.data.utils.Constants.ColumnIndex.SATURATED_FAT
import org.seoulsquad.data.utils.Constants.ColumnIndex.SODIUM
import org.seoulsquad.data.utils.Constants.ColumnIndex.SUGAR
import org.seoulsquad.data.utils.Constants.ColumnIndex.TOTAL_FAT
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_CONTRIBUTOR_ID
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_DESCRIPTION
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_ID
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_INGREDIENTS
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_MINUTES
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_NAME
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_N_INGREDIENTS
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_N_STEPS
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_STEPS
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_SUBMITTED
import org.seoulsquad.data.utils.Constants.ColumnName.COLUMN_TAGS
import data.model.Meal
import org.seoulsquad.model.Nutrition

class MealCsvParser {

    fun parse(headers: List<String>, values: List<String>): Meal {
        val valueMap = headers.zip(values).toMap()
        val nutritionList = parseNumberArrayString(valueMap["nutrition"] ?: "[]")

        return Meal(
            id = valueMap[COLUMN_ID]?.toIntOrNull() ?: 0,
            name = valueMap[COLUMN_NAME] ?: "",
            minutes = valueMap[COLUMN_MINUTES]?.toIntOrNull() ?: 0,
            contributorId = valueMap[COLUMN_CONTRIBUTOR_ID]?.toIntOrNull() ?: 0,
            submitted = parseDate(valueMap[COLUMN_SUBMITTED]),
            tags = parseArrayString(valueMap[COLUMN_TAGS] ?: "[]"),
            nutrition = Nutrition(
                calories = nutritionList.getOrElse(CALORIES) { 0.0 },
                totalFat = nutritionList.getOrElse(TOTAL_FAT) { 0.0 },
                sugar = nutritionList.getOrElse(SUGAR) { 0.0 },
                sodium = nutritionList.getOrElse(SODIUM) { 0.0 },
                protein = nutritionList.getOrElse(PROTEIN) { 0.0 },
                saturatedFat = nutritionList.getOrElse(SATURATED_FAT) { 0.0 },
                carbohydrates = nutritionList.getOrElse(CARBOHYDRATES) { 0.0 }
            ),
            numberOfSteps = valueMap[COLUMN_N_STEPS]?.toIntOrNull() ?: 0,
            steps = parseArrayString(valueMap[COLUMN_STEPS] ?: "[]"),
            description = valueMap[COLUMN_DESCRIPTION]?.takeIf { it.isNotBlank() },
            ingredients = parseArrayString(valueMap[COLUMN_INGREDIENTS] ?: "[]"),
            numberOfIngredients = valueMap[COLUMN_N_INGREDIENTS]?.toIntOrNull() ?: 0
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