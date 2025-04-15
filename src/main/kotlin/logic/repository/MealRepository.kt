package org.seoulsquad.logic.repository

import org.seoulsquad.model.Meal

interface MealRepository {
    fun getAllMeals(): List<Meal>
}