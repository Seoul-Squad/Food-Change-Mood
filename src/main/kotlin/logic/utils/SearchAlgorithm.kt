package org.seoulsquad.logic.utils

import logic.model.Meal

interface SearchAlgorithm {
    fun search(meals: List<Meal>, query: String): Result<List<Meal>>
}