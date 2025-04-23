package testData

import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.model.Nutrition

fun createItalianLargeMeal(tags: List<String>, desc: String? = null) = Meal(
    id = 0,
    name = "",
    preparationTimeInMinutes = 0,
    contributorId = 1,
    submittedAt = LocalDate.parse("2025-04-04"),
    tags = tags,
    nutrition = Nutrition(73.7, 8.0, 13.0, 0.0, 2.0, 3.0, 1.0),
    numberOfSteps = 0,
    steps = listOf(),
    description = desc,
    numberOfIngredients = 0,
    ingredients = listOf()
)
