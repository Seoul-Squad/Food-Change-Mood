package mockData

import logic.model.Meal
import logic.model.Nutrition

fun createMeal(
    id:Int,
    name:String,
    description:String?,
    tags: List<String>,
)=Meal(
    name = name,
    description = description,
    tags = tags,
    id = id,
    ingredients = emptyList(),
    numberOfIngredients = 0,
    steps = emptyList(),
    nutrition = Nutrition(
        calories = 0.0,
        protein = 0.0,
        carbohydrates = 0.0,
        sugar = 0.0,
        sodium = 0.0,
        totalFat = 0.0,
        saturatedFat = 0.0,
    ),
    preparationTimeInMinutes = 0,
    numberOfSteps = 0,
    submittedAt =  null,
    contributorId = 0
)
