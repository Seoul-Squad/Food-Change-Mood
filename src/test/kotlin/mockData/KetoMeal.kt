package mockData

import logic.model.Meal
import logic.model.Nutrition


fun createMeal(
    totalFat: Double,
    protein: Double,
    carbohydrates: Double,
    sugar: Double

) = Meal(
    name = "",
    description = "",
    tags = emptyList(),
    id = 1,
    ingredients = emptyList(),
    numberOfIngredients = 0,
    steps = emptyList(),
    nutrition = Nutrition(
        0.0,
        totalFat,
        sugar,
        0.0,
        protein,
        0.0,
        carbohydrates
    ),
    preparationTimeInMinutes = 0,
    numberOfSteps = 0,
    submittedAt = null,
    contributorId = 0
)
