package data.utils

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileReader

class CsvFileReaderTest {

    private val parser = CsvLineParser()

    @Test
    fun `should return headers and rows when CSV is read`() {
        // Given
        val content = "name,id\nkoshary,30\nboundry,25"
        val tempFile = createTempFileWithContent(content)
        // When
        val reader = CsvFileReader(FileReader(tempFile), parser)
        val result = reader.readCsv()
        // Then
        assertThat(result.headers).isEqualTo(listOf("name", "id"))
        assertThat(result.rows).isEqualTo(
            listOf(
                listOf("koshary", "30"),
                listOf("boundry", "25")
            )
        )

        tempFile.deleteOnExit()
    }

    @Test
    fun `should return empty CsvData for empty input`() {
        // Given
        val content = ""
        val tempFile = createTempFileWithContent(content)
        // When
        val reader = CsvFileReader(FileReader(tempFile), parser)
        val result = reader.readCsv()
        // Then
        assertThat(result.headers).isEmpty()
        assertThat(result.rows).isEmpty()

        tempFile.deleteOnExit()
    }

    @Test
    fun `should skip row when size is less than headers`() {
        // Given
        val content = "name,id\nkoshary"
        val tempFile = createTempFileWithContent(content)
        // When
        val reader = CsvFileReader(FileReader(tempFile), parser)
        val result = reader.readCsv()
        // Then
        assertThat(result.headers).isEqualTo(listOf("name", "id"))
        assertThat(result.rows).isEmpty()

        tempFile.deleteOnExit()
    }

    private fun createTempFileWithContent(content: String): File {
        return File.createTempFile("test", ".csv").apply {
            writeText(content)
        }
    }
}
