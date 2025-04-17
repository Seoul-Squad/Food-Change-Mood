package org.seoulsquad.presentation

import org.seoulsquad.logic.useCase.GetRandomIngredientQuestion

class IngredientGameUi(
    private val getRandomIngredientQuestion: GetRandomIngredientQuestion,
) {
    fun startIngredientGame() {
        var score = 0
        var roundNumber = 1
        var isGameOver = false
        while (roundNumber <= 15 && !isGameOver) {
            val ingredientQuestion = getRandomIngredientQuestion()

            ingredientQuestion
                .onSuccess { question ->
                    val meal = question.meal
                    val chooses = question.chooses
                    println("What is the right ingredient for: ${meal.name}")

                    chooses.forEachIndexed { index, choose ->
                        println("${index.inc()}- ${choose.second}")
                    }

                    val answer = readlnOrNull() ?: ""

                    if (answer.isNotBlank()) {
                        if (chooses[answer.toInt().dec()].first) {
                            score += 1000
                            roundNumber++
                        } else {
                            println("Wrong answer, Game Over")
                            isGameOver = true
                        }
                    } else {
                        println("Invalid input")
                    }
                }.onFailure { error ->
                    println(error.message)
                }
        }
        println("your score: $score")
    }
}
