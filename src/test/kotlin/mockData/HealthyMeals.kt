package mockData

import logic.model.Meal
import logic.model.Nutrition

fun createMeal(
    name: String,
    totalFat: Double,
    satFat: Double,
    carbs: Double,
    prepTime: Int

) = Meal(
    name = name,
    description = "",
    tags = emptyList(),
    id = 1,
    ingredients = emptyList(),
    numberOfIngredients = 0,
    steps = emptyList(),
    nutrition = Nutrition(
        0.0,
        totalFat,
        0.0,
        0.0,
        0.0,
        satFat,
        carbs
    ),
    preparationTimeInMinutes = prepTime,
    numberOfSteps = 0,
    submittedAt = null,
    contributorId = 0
)