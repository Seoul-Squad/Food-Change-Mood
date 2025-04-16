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
}
