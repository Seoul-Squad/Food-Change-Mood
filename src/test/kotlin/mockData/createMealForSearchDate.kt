package mockData

import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.model.Nutrition

fun createMealForSearchDate(
    id: Int,
    nameOfMeal: String,
    date: LocalDate?,
) = Meal(
    id = id,
    name = nameOfMeal,
    submittedAt = date,
    preparationTimeInMinutes = 0,
    contributorId = 0,
    tags = emptyList(),
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
    ingredients = emptyList(),
    numberOfIngredients = 0,

    )