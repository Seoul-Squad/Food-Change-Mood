package mockData

import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.model.Nutrition

fun createSearchByNameMeal(name: String) = Meal(
    id = 0,
    name = name,
    preparationTimeInMinutes = 0,
    contributorId = 1,
    submittedAt = LocalDate.parse("2025-04-04"),
    tags = listOf(),
    nutrition = Nutrition(73.7, 8.0, 13.0, 0.0, 2.0, 3.0, 1.0),
    numberOfSteps = 0,
    steps = listOf(),
    description = null,
    numberOfIngredients = 0,
    ingredients = listOf()
)
