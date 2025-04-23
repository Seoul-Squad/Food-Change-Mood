package org.seoulsquad.presentation.consolelIO

import kotlinx.datetime.LocalDate

class ConsoleReader : Reader {
    override fun readString(): String? = readlnOrNull()

    override fun readInt(): Int? = readlnOrNull()?.toIntOrNull()

    override fun readDouble(): Double? = readlnOrNull()?.toDoubleOrNull()

    override fun readDate(): LocalDate? {
        TODO("Not yet implemented Ya Khaled")
    }
}
