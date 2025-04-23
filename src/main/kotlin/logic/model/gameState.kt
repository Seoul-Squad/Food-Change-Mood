package org.seoulsquad.logic.model

import logic.model.Meal

data class GameState(
    val currentMeal: Meal?,
    val currentAttempt: Int,
    val maxAttempts: Int,
    val shouldStartNewRound: Boolean
)
