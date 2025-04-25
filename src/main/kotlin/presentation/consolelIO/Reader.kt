package org.seoulsquad.presentation.consolelIO

interface Reader {
    fun readString(): String

    fun readInt(): Int?

    fun readDouble(): Double?
}
