package org.seoulsquad.presentation.consolelIO

import kotlinx.datetime.LocalDate

interface Reader {
    fun readString(): String

    fun readInt(): Int?

    fun readDouble(): Double?

    fun readDate(): LocalDate?
}
