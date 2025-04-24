package presentation.consolelIO

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.seoulsquad.presentation.consolelIO.ConsoleViewer
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test

class ConsoleViewerTest{
    private val consoleViewer = ConsoleViewer()
    private val originalOut = System.out
    private val outputStream = ByteArrayOutputStream()
    private val printStream = PrintStream(outputStream)

    @BeforeEach
    fun setUp() {
        System.setOut(printStream)
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
        outputStream.reset()
    }

    @Test
    fun `debug newline issue`() {
        consoleViewer.display("Test")

        //Then
        val actual = outputStream.toString()
        val expected = "Test\r\n"
        assertEquals(expected, actual)
    }

    @Test
    fun `should print message with special characters to console`() {
        val message = "Hello\nWorld\t!"
        val expected = "$message${System.lineSeparator()}"
        consoleViewer.display(message)
        assertEquals(expected, outputStream.toString())
    }

    @Test
    fun `should print multiple messages correctly`() {
        val message1 = "First"
        val message2 = "Second"
        val expected = "$message1${System.lineSeparator()}$message2${System.lineSeparator()}"
        consoleViewer.display(message1)
        consoleViewer.display(message2)
        assertEquals(expected, outputStream.toString())
    }
}