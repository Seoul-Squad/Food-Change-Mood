package data.repository

import com.google.common.truth.Truth.assertThat
import data.model.CsvData
import data.utils.CsvFileReader
import data.utils.MealCsvParser
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.model.Nutrition
import org.seoulsquad.data.repository.MealRepositoryImpl
import kotlin.test.Test

class MealRepositoryImplTest {

    private val fileReader: CsvFileReader = mockk()
    private val parser: MealCsvParser = mockk()

    private val headers = listOf("id", "name")
    private val row = listOf("1", "Test Meal")

    private val expectedMeal = Meal(
        id = 1,
        name = "Test Meal",
        preparationTimeInMinutes = 0,
        contributorId = 0,
        submittedAt = null,
        tags = emptyList(),
        nutrition = Nutrition(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        numberOfSteps = 0,
        steps = emptyList(),
        description = null,
        ingredients = emptyList(),
        numberOfIngredients = 0
    )

    @Test
    fun `should return all meals when all rows are parsed successfully`() {
        // Given
        every { fileReader.readCsv() } returns CsvData(headers, listOf(row))
        every { parser.parse(headers, row) } returns expectedMeal
        val repository = MealRepositoryImpl(fileReader, parser)

        // When
        val result = repository.getAllMeals()

        // Then
        assertThat(result).containsExactly(expectedMeal)
    }

    @Test
    fun `should return empty list when headers are empty`() {
        // Given
        every { fileReader.readCsv() } returns CsvData(emptyList(), listOf(row))
        val repository = MealRepositoryImpl(fileReader, parser)

        // When
        val result = repository.getAllMeals()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when rows are empty`() {
        // Given
        every { fileReader.readCsv() } returns CsvData(headers, emptyList())
        val repository = MealRepositoryImpl(fileReader, parser)

        // When
        val result = repository.getAllMeals()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when row parsing fails`() {
        // Given
        every { fileReader.readCsv() } returns CsvData(headers, listOf(row))
        every { parser.parse(headers, row) } throws IllegalArgumentException("Invalid format")
        val repository = MealRepositoryImpl(fileReader, parser)

        // When
        val result = repository.getAllMeals()

        // Then
        assertThat(result).isEmpty()
    }
}