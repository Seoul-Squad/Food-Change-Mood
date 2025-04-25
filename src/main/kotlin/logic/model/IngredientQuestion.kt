package org.seoulsquad.logic.model

data class IngredientQuestion(
    val mealName: String,
    val chooses: List<Pair<Boolean, String>>,
)
