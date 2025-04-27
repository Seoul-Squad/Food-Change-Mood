package presentation.consolelIO

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.seoulsquad.presentation.consolelIO.ConsoleViewer
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
//
//class ConsoleViewerTest {
//    private val consoleViewer = ConsoleViewer()
//    private val originalOut = System.out
//    private val outputStream = ByteArrayOutputStream()
//    private val printStream = PrintStream(outputStream)
//
//    @BeforeEach
//    fun setUp() {
//        System.setOut(printStream)
//    }
//
//    @AfterEach
//    fun tearDown() {
//        System.setOut(originalOut)
//        outputStream.reset()
//    }
//
//    @Test
//    fun `should return message followed by CRLF when display is called`() {
//        // Given
//        val message = "Test"
//
//        // When
//        consoleViewer.display(message)
//
//        // Then
//        val actual = outputStream.toString()
//        val expected = "Test\r\n"
//        assertThat(actual).isEqualTo(expected)
//    }
//
//    @Test
//    fun `should return formatted message with special characters when display is called`() {
//        // Given
//        val message = "Hello\nWorld\t!"
//        val expected = "$message${System.lineSeparator()}"
//
//        // When
//        consoleViewer.display(message)
//
//        // Then
//        assertThat(outputStream.toString()).isEqualTo(expected)
//    }
//
//    @Test
//    fun `should return multiple lines correctly when display is called multiple times`() {
//        // Given
//        val message1 = "First"
//        val message2 = "Second"
//        val expected = "$message1${System.lineSeparator()}$message2${System.lineSeparator()}"
//
//        // When
//        consoleViewer.display(message1)
//        consoleViewer.display(message2)
//
//        // Then
//        assertThat(outputStream.toString()).isEqualTo(expected)
//    }
//}


