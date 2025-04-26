package org.seoulsquad.presentation.consolelIO

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.InputStream

class ConsoleReaderTest {

    private lateinit var consoleReader: ConsoleReader

    @BeforeEach
    fun setUp() {
        consoleReader = ConsoleReader()
    }

    private fun provideInput(input: String) {
        val testInput: InputStream = ByteArrayInputStream(input.toByteArray())
        System.setIn(testInput)
    }

    // readString
    @Test
    fun `should return string when valid input is entered`() {
        provideInput("Hello\n")

        val result = consoleReader.readString()

        assertEquals("Hello", result)
    }

    // readInt - valid
    @Test
    fun `should return int when valid integer is entered`() {
        provideInput("42\n")

        val result = consoleReader.readInt()

        assertEquals(42, result)
    }

    // readInt - null when input is blank
    @Test
    fun `should return null when input is blank for readInt`() {
        provideInput("\n")

        val result = consoleReader.readInt()

        assertEquals(null, result)
    }

    // readInt - null when input is non-numeric
    @Test
    fun `should return null when input is not a valid integer`() {
        provideInput("abc\n")

        val result = consoleReader.readInt()

        assertEquals(null, result)
    }

    // readDouble - valid
    @Test
    fun `should return double when valid decimal number is entered`() {
        provideInput("3.14\n")

        val result = consoleReader.readDouble()

        assertEquals(3.14, result)
    }

    // readDouble - null when input is blank
    @Test
    fun `should return null when input is blank for readDouble`() {
        provideInput("\n")

        val result = consoleReader.readDouble()

        assertEquals(null, result)
    }

    // readDouble - null when input is not a valid number
    @Test
    fun `should return null when input is not a valid double`() {
        provideInput("xyz\n")

        val result = consoleReader.readDouble()

        assertEquals(null, result)
    }

    @Test
    fun `readInt should return null when input is blank`() {
        provideInput("\n")
        val result = consoleReader.readInt()
        assertEquals(null, result)
    }

    @Test
    fun `readInt should return null when input is invalid number`() {
        provideInput("abc\n")
        val result = consoleReader.readInt()
        assertEquals(null, result)
    }

    @Test
    fun `readInt should return integer when input is valid`() {
        provideInput("123\n")
        val result = consoleReader.readInt()
        assertEquals(123, result)
    }


    @Test
    fun `readDouble should return null when input is blank`() {
        provideInput("\n")
        val result = consoleReader.readDouble()
        assertEquals(null, result)
    }

    @Test
    fun `readDouble should return null when input is invalid number`() {
        provideInput("abc\n")
        val result = consoleReader.readDouble()
        assertEquals(null, result)
    }

    @Test
    fun `readDouble should return double when input is valid`() {
        provideInput("3.14\n")
        val result = consoleReader.readDouble()
        assertEquals(3.14, result)
    }


}
