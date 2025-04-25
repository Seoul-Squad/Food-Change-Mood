package mockData

import logic.model.Meal
import logic.model.Nutrition

fun creatIraqiMeals(
    id: Int,
    tags: List<String> ,
    description: String?,
) = Meal(
    name = "",
    id = id,
    preparationTimeInMinutes = 0,
    contributorId = 0,
    submittedAt = null,
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
    description = description,
    ingredients = emptyList(),
    numberOfIngredients = 0
)