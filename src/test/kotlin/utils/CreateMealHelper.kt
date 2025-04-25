package utils

import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.model.Nutrition

fun createMeal(
    id: Int,
    name: String,
    tags: List<String>,
    ingredients: List<String>
) = Meal(
    name = name,
    id = id,
    preparationTimeInMinutes = 0,
    contributorId = 0,
    submittedAt = LocalDate(1970, 1, 1),
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
    description = "",
    ingredients = ingredients,
    numberOfIngredients = 0
)

fun createMeal(
    id: Int,
    name: String,
    tags: List<String>,
    nutrition: Nutrition
) = Meal(
    name = name,
    id = id,
    preparationTimeInMinutes = 0,
    contributorId = 0,
    submittedAt = LocalDate(1970, 1, 1),
    tags = tags,
    nutrition = nutrition,
    numberOfSteps = 0,
    steps = emptyList(),
    description = null,
    ingredients = emptyList(),
    numberOfIngredients = 0
)

fun createNutritionWithProtein(protein: Double) = Nutrition(
    calories = 0.0,
    totalFat = 0.0,
    sugar = 0.0,
    sodium = 0.0,
    protein = protein,
    saturatedFat = 0.0,
    carbohydrates = 0.0
)