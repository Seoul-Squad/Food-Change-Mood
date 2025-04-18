package org.seoulsquad.logic.utils

import logic.model.Meal

class KnuthMorrisPrattSearchAlgorithm : SearchAlgorithm {

    override fun search(meals: List<Meal>, query: String): Result<List<Meal>> {
        if (query.isBlank()) return Result.failure(Exception("Name cannot be empty"))
        val lowerCaseQuery = query.lowercase()
        val lps = computeLPSArray(lowerCaseQuery)


        return meals.filter {
            kmpSearch(it.name, lowerCaseQuery, lps)
        }.takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: LevenshteinSearchAlgorithm().search(meals, query)
    }

    private fun computeLPSArray(pattern: String): IntArray {
        val m = pattern.length
        val lps = IntArray(m)
        var length = 0 // Length of the previous longest prefix suffix
        var i = 1
        if (m > 0) lps[0] = 0 // lps[0] is always 0

        while (i < m) {
            // Use lowercase comparison for case-insensitivity during LPS computation
            if (pattern[i].lowercaseChar() == pattern[length].lowercaseChar()) {
                length++
                lps[i] = length
                i++
            } else {
                if (length != 0) {
                    length = lps[length - 1]
                } else {
                    lps[i] = 0
                    i++
                }
            }
        }
        return lps
    }

    private fun kmpSearch(text: String, pattern: String, lpsArray: IntArray): Boolean {
        val n = text.length
        val m = pattern.length
        if (m == 0) return true
        if (n == 0 || m > n) return false

        var i = 0
        var j = 0
        while (i < n) {
            if (pattern[j].lowercaseChar() == text[i].lowercaseChar()) {
                i++
                j++
            }

            if (j == m) {
                return true // Found
            } else if (i < n && pattern[j].lowercaseChar() != text[i].lowercaseChar()) {
                if (j != 0) {
                    j = lpsArray[j - 1]
                } else {
                    i++
                }
            }
        }
        return false
    }
}