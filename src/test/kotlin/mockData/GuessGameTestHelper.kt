package mockData

import logic.model.Nutrition
import kotlinx.datetime.LocalDate
import logic.model.Meal

private val nutrition = Nutrition(
    calories = 0.0,
    totalFat = 0.0,
    sugar = 0.0,
    sodium = 0.0,
    protein = 0.0,
    saturatedFat = 0.0,
    carbohydrates = 0.0
)

fun createMeal(
    name: String,
    preparationTimeInMinutes: Int
): Meal = Meal(
    name = name,
    id = 0,
    preparationTimeInMinutes = preparationTimeInMinutes,
    contributorId = 0,
    submittedAt = LocalDate.parse("2024-01-01"),
    tags = emptyList(),
    nutrition = nutrition,
    numberOfSteps = 0,
    steps = emptyList(),
    description = "This is a random meal",
    ingredients = emptyList(),
    numberOfIngredients = 0
)