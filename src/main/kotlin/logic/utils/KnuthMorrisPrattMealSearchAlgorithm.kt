package org.seoulsquad.logic.utils

import logic.model.Meal
import logic.utils.NoMealsFoundException

class KnuthMorrisPrattMealSearchAlgorithm {

    fun searchMeal(meals: List<Meal>, query: String): Result<List<Meal>> {
        val longestPrefixString = computeLongestPrefixStringArray(query.lowercase())
        return meals.filter {
            knuthMorrisPrattMealSearch(it.name.lowercase(), query.lowercase(), longestPrefixString)
        }.takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoMealsFoundException())
    }

    private fun computeLongestPrefixStringArray(pattern: String): IntArray {
        val patternLength = pattern.length
        val longestPrefixString = IntArray(patternLength)
        var longestPrefixStringLength = 0
        var indexOfComparison = 1
        longestPrefixString[0] = 0

        while (indexOfComparison < patternLength) {
            if (pattern[indexOfComparison].lowercaseChar() == pattern[longestPrefixStringLength].lowercaseChar()) {
                longestPrefixStringLength++
                longestPrefixString[indexOfComparison] = longestPrefixStringLength
                indexOfComparison++
            } else {
                if (longestPrefixStringLength != 0) {
                    longestPrefixStringLength = longestPrefixString[longestPrefixStringLength - 1]
                } else {
                    longestPrefixString[indexOfComparison] = 0
                    indexOfComparison++
                }
            }
        }
        return longestPrefixString
    }

    private fun knuthMorrisPrattMealSearch(text: String, pattern: String, longestPrefixStringArray: IntArray): Boolean {
        val textLength = text.length
        val patternLength = pattern.length
        if (textLength == 0 || patternLength > textLength) return false
        var textIndex = 0
        var patternIndex = 0
        while (textIndex < textLength) {
            if (pattern[patternIndex].lowercaseChar() == text[textIndex].lowercaseChar()) {
                textIndex++
                patternIndex++
            }

            if (patternIndex == patternLength) {
                return true
            } else if (textIndex < textLength && pattern[patternIndex].lowercaseChar() != text[textIndex].lowercaseChar()) {
                if (patternIndex != 0) {
                    patternIndex = longestPrefixStringArray[patternIndex - 1]
                } else {
                    textIndex++
                }
            }
        }
        return false
    }
}