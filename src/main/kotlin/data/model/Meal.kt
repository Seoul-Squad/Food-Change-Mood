package data.model

import kotlinx.datetime.LocalDate
import org.seoulsquad.model.Nutrition

data class Meal(
    val name: String,
    val id: Int,
    val minutes: Int,
    val contributorId: Int,
    val submitted: LocalDate?,
    val tags: List<String>,
    val nutrition: Nutrition,
    val numberOfSteps: Int,
    val steps: List<String>,
    val description: String?,
    val ingredients: List<String>,
    val numberOfIngredients: Int,
)



