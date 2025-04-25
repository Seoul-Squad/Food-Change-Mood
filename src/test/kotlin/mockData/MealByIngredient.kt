package mockData

import logic.model.Meal
import logic.model.Nutrition

fun createMeal(
    id: Int,
    ingredients: List<String>,
) = Meal(
    name = "",
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
    description = "",
    ingredients = ingredients,
    numberOfIngredients = 0,
)
