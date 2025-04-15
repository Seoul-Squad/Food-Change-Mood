package org.seoulsquad.logic.repository

import data.model.Meal

interface MealRepository {
    fun getAllMeals(): List<Meal>
}