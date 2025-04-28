package org.seoulsquad.presentation.consolelIO

class ConsoleReader : Reader {
    override fun readString(): String = readln()

    override fun readInt(): Int? = readln().toIntOrNull()

    override fun readDouble(): Double? = readln().toDoubleOrNull()

}

