package data.utils

object Constants {
    const val CSV_FILE_PATH = "food.csv"

    object ColumnName {
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_MINUTES = "minutes"
        const val COLUMN_CONTRIBUTOR_ID = "contributor_id"
        const val COLUMN_SUBMITTED = "submitted"
        const val COLUMN_TAGS = "tags"
        const val COLUMN_N_STEPS = "n_steps"
        const val COLUMN_STEPS = "steps"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_INGREDIENTS = "ingredients"
        const val COLUMN_N_INGREDIENTS = "n_ingredients"
    }

    object ColumnIndex {
        const val CALORIES = 0
        const val TOTAL_FAT = 1
        const val SUGAR = 2
        const val SODIUM = 3
        const val PROTEIN = 4
        const val SATURATED_FAT = 5
        const val CARBOHYDRATES = 6
    }

    object NutritionConstants {
        const val CALORIES_PER_GRAM_FAT = 9
        const val CALORIES_PER_GRAM_PROTEIN = 4
        const val CALORIES_PER_GRAM_CARBS = 4

        const val MAX_NET_CARBS = 20
        const val MAX_SUGAR = 5

        const val FAT_PERCENT_MIN = 70.0
        const val FAT_PERCENT_MAX = 75.0

        const val PROTEIN_PERCENT_MIN = 20.0
        const val PROTEIN_PERCENT_MAX = 25.0

        const val CARB_PERCENT_MIN = 5.0
        const val CARB_PERCENT_MAX = 10.0
    }
}
