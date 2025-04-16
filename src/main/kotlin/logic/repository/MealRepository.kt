package logic.repository

import logic.model.Meal

interface MealRepository {
    fun getAllMeals(): List<Meal>
}