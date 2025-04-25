package org.seoulsquad.logic.model

data class IngredientQuestion(
    val id: Int,
    val mealName: String,
    val chooses: List<Pair<Boolean, String>>,
)
