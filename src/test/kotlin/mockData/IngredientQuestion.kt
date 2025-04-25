package mockData

import logic.model.Meal
import logic.model.Nutrition

fun createMeal(
    id: Int,
    name: String,
    ingredients: List<String>,
) = Meal(
    name = name,
    id = id,
    preparationTimeInMinutes = 0,
    contributorId = 0,
    submittedAt = null,
    tags = emptyList(),
    nutrition =
        Nutrition(
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
        ),
    numberOfSteps = 0,
    steps = emptyList(),
    description = null,
    ingredients = ingredients,
    numberOfIngredients = 0,
)
