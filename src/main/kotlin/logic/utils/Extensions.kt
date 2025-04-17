package org.seoulsquad.logic.utils


fun List<Double>.precntage(precentage: Double): Double {
    if (isEmpty()) return 0.0
    val sorted = this.sorted()
    val index = (precentage * (sorted.size - 1)).toInt()
    return sorted[index]
}