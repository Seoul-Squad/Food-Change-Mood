package mockData

import logic.model.Meal
import logic.model.Nutrition

private val nutrition = Nutrition(0.0,0.0,0.0, 0.0, 0.0, 0.0, 0.0)

fun createMeal(id: Int, preparationTimeInMinutes: Int, numberOfIngredients: Int, numberOfSteps: Int) =
    Meal(
    name = "Random meal",
    id = id,
    preparationTimeInMinutes = preparationTimeInMinutes,
    contributorId = 0,
    submittedAt = null,
    tags = emptyList(),
    nutrition = nutrition,
    numberOfSteps = numberOfSteps,
    steps = List(numberOfSteps) { "Step $it" },
    description = "This is a random meal",
    ingredients = List(numberOfIngredients) { "Ingredient $it" },
    numberOfIngredients = numberOfIngredients
    )