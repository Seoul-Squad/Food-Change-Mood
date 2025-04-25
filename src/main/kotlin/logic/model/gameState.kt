package org.seoulsquad.logic.model

import logic.model.Meal

data class PreperationTimeGameState(
    val currentMeal: Meal?,
    val currentAttempt: Int,
    val maxAttempts: Int,
    val shouldStartNewRound: Boolean
)
