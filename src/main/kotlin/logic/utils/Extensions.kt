package org.seoulsquad.logic.utils


fun List<Double>.percentage(percentage: Double): Double {
    if (isEmpty()) return 0.0
    val sorted = this.sorted()
    val index = (percentage * (sorted.size - 1)).toInt()
    return sorted[index]
}