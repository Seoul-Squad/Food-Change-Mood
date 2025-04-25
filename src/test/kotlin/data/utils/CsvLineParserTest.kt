package data.utils

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class CsvLineParserTest {

    private val parser = CsvLineParser()

    @Test
    fun `should return list of meals when given simple comma-separated values`() {
        // Given
        val line = "tacos,pizza,salad"
        // When
        val result = parser.parseCsvLine(line)
        // Then
        assertThat(result).isEqualTo(listOf("tacos", "pizza", "salad"))
    }

    @Test
    fun `should return correct values when meal names are quoted and contain commas`() {
        // Given
        val line = "\"chicken curry\",\"beef, stew\",lasagna"
        // When
        val result = parser.parseCsvLine(line)
        // Then
        assertThat(result).isEqualTo(listOf("chicken curry", "beef, stew", "lasagna"))
    }

    @Test
    fun `should return trimmed meal names when fields have surrounding whitespace`() {
        // Given
        val line = "  risotto  ,  ramen , paella "
        // When
        val result = parser.parseCsvLine(line)
        // Then
        assertThat(result).isEqualTo(listOf("risotto", "ramen", "paella"))
    }

    @Test
    fun `should return empty string when meal field is missing`() {
        // Given
        val line = "soup,,burger"
        // When
        val result = parser.parseCsvLine(line)
        // Then
        assertThat(result).isEqualTo(listOf("soup", "", "burger"))
    }

    @Test
    fun `should return empty strings when quoted fields are empty`() {
        // Given
        val line = "\"pasta\",,\"\",\"enchiladas\""
        // When
        val result = parser.parseCsvLine(line)
        // Then
        assertThat(result).isEqualTo(listOf("pasta", "", "", "enchiladas"))
    }

    @Test
    fun `should return single field with comma when value is quoted`() {
        // Given
        val line = "\"spaghetti, carbonara\""
        // When
        val result = parser.parseCsvLine(line)
        // Then
        assertThat(result).isEqualTo(listOf("spaghetti, carbonara"))
    }

    @Test
    fun `should return last field as empty string when line ends with comma`() {
        // Given
        val line = "gnocchi,risotto,"
        // When
        val result = parser.parseCsvLine(line)
        // Then
        assertThat(result).isEqualTo(listOf("gnocchi", "risotto", ""))
    }

    @Test
    fun `should return single empty string when line is empty`() {
        // Given
        val line = ""
        // When
        val result = parser.parseCsvLine(line)
        // Then
        assertThat(result).isEqualTo(listOf(""))
    }
}