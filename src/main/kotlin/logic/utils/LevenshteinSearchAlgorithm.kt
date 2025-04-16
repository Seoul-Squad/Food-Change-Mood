package org.seoulsquad.logic.utils

import logic.model.Meal

class LevenshteinSearchAlgorithm(
    private val maxDistance: Int = 2,
) : SearchAlgorithm {
    override fun search(meals: List<Meal>, query: String): Result<List<Meal>> {
        if (query.isBlank()) return Result.failure(Exception("Name cannot be empty"))
        val lowerCaseQuery = query.lowercase()
        return meals.filter {
            val distance = calculateFunctionalLevenshtein(it.name.lowercase(), lowerCaseQuery)
            distance <= maxDistance
        }.takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("No Meals found"))

    }

    private fun calculateFunctionalLevenshtein(s1: String, s2: String): Int {
        val len1 = s1.length
        val len2 = s2.length
        if (len1 == 0) return len2
        if (len2 == 0) return len1
        val initialRow: List<Int> = (0..len2).toList()
        val finalRow = s1.foldIndexed(initialRow) { i, previousRow, char1 ->
            val firstElementOfCurrentRow = i + 1

            val currentRowElements = (1..len2).scan(firstElementOfCurrentRow) { leftValue, j ->
                val cost = if (char1 == s2[j - 1]) 0 else 1

                minOf(
                    previousRow[j] + 1,
                    leftValue + 1,
                    previousRow[j - 1] + cost
                )
            }
            currentRowElements
        }
        return finalRow.last()
    }
}