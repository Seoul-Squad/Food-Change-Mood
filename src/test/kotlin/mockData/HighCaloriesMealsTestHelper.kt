package mockData

import logic.model.Meal
import logic.model.Nutrition


fun createMeal(id: Int, calories: Double): Meal {
    val nutrition = Nutrition(calories,0.0,0.0, 0.0, 0.0, 0.0, 0.0)

    return Meal(
        name = "Random meal",
        id = id,
        preparationTimeInMinutes = 0,
        contributorId = 0,
        submittedAt = null,
        tags = emptyList(),
        nutrition = nutrition,
        numberOfSteps = 0,
        steps = emptyList(),
        description = "This is a random meal",
        ingredients = emptyList(),
        numberOfIngredients = 0
    )
}
