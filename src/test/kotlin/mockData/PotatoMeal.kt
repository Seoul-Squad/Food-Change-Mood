package mockData

import logic.model.Meal
import logic.model.Nutrition

fun createPotatoMeal(ingredients: List<String>) =
    Meal(
        name = "",
        id = 1,
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
