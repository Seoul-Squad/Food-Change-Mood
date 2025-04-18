package org.seoulsquad.logic.model

import logic.model.Meal

data class IngredientQuestion(
    val meal: Meal,
    val chooses: List<Pair<Boolean, String>>,
)
