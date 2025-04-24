package utils

import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.model.Nutrition

fun createMeal(
    name: String,
    tags: List<String>,
    ingredients: List<String>
) = Meal(
    name = name,
    id = 0,
    preparationTimeInMinutes = 0,
    contributorId = 0,
    submittedAt = LocalDate(1970, 1, 1),
    tags = tags,
    nutrition = Nutrition(
        calories = 0.0,
        totalFat = 0.0,
        sugar = 0.0,
        sodium = 0.0,
        protein = 0.0,
        saturatedFat = 0.0,
        carbohydrates = 0.0
    ),
    numberOfSteps = 0,
    steps = emptyList(),
    description = "",
    ingredients = ingredients,
    numberOfIngredients = 0
)
