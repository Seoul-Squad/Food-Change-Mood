package data.utils

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.model.Nutrition
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class MealCsvParserTest {

    private val parser = MealCsvParser()

    // Shared meal objects for expected results
    private val spaghettiMeal = Meal(
        id = 1,
        name = "Spaghetti Bolognese",
        preparationTimeInMinutes = 45,
        contributorId = 1001,
        submittedAt = LocalDate(2023, 10, 12),
        tags = listOf("dinner", "italian"),
        nutrition = Nutrition(400.5, 15.0, 5.0, 800.0, 20.0, 6.0, 50.0),
        numberOfSteps = 5,
        steps = listOf("Boil water", "Cook pasta", "Prepare sauce", "Mix", "Serve"),
        description = "Classic Italian pasta dish",
        ingredients = listOf("pasta", "meat", "tomato"),
        numberOfIngredients = 3
    )

    private val defaultMeal = Meal(
        id = 0,
        name = "",
        preparationTimeInMinutes = 0,
        contributorId = 0,
        submittedAt = null,
        tags = emptyList(),
        nutrition = Nutrition(0.0,0.0,0.0,0.0,0.0,0.0,0.0),
        numberOfSteps = 0,
        steps = emptyList(),
        description = null,
        ingredients = emptyList(),
        numberOfIngredients = 0
    )

    private val testMeal = Meal(
        id = 1,
        name = "Test Meal",
        preparationTimeInMinutes = 0,
        contributorId = 0,
        submittedAt = null,
        tags = emptyList(),
        nutrition = Nutrition(0.0,0.0,0.0,0.0,0.0,0.0,0.0),
        numberOfSteps = 0,
        steps = emptyList(),
        description = null,
        ingredients = emptyList(),
        numberOfIngredients = 0
    )

    @Test
    fun `should return meal with all fields correctly parsed when input is valid`() {
        // Given
        val headers = listOf("id", "name", "minutes", "contributor_id", "submitted", "tags", "nutrition", "n_steps", "steps", "description", "ingredients", "n_ingredients")
        val values = listOf("1", "Spaghetti Bolognese", "45", "1001", "2023-10-12", "['dinner', 'italian']", "[400.5, 15.0, 5.0, 800.0, 20.0, 6.0, 50.0]", "5", "['Boil water', 'Cook pasta', 'Prepare sauce', 'Mix', 'Serve']", "Classic Italian pasta dish", "['pasta', 'meat', 'tomato']", "3")

        // When
        val result = parser.parse(headers, values)

        // Then
        assertThat(result).isEqualTo(spaghettiMeal)
    }

    @Test
    fun `should return default meal when input is blank or invalid`() {
        // Given
        val headers = listOf("id", "name", "minutes", "contributor_id", "submitted", "tags", "nutrition", "n_steps", "steps", "description", "ingredients", "n_ingredients")
        val values = List(headers.size) { "" }

        // When
        val result = parser.parse(headers, values)

        // Then
        assertThat(result).isEqualTo(defaultMeal)
    }

    @Test
    fun `should return partially parsed meal when array is malformed`() {
        // Given
        val headers = listOf("id", "name", "tags")
        val values = listOf("1", "Test Meal", "[dinner, 'italian',]")

        // When
        val result = parser.parse(headers, values)

        // Then
        assertThat(result.copy(tags = emptyList())).isEqualTo(testMeal)
        assertThat(result.tags).isEqualTo(listOf("dinner", "italian"))
    }

    @Test
    fun `should return meal without empty elements when array contains empty string`() {
        // Given
        val headers = listOf("id", "name", "ingredients")
        val values = listOf("1", "Test Meal", "['pasta', '', 'tomato']")

        // When
        val result = parser.parse(headers, values)

        // Then
        assertThat(result.ingredients).isEqualTo(listOf("pasta", "tomato"))
    }

    @Test
    fun `should return meal with defaults when required headers are missing`() {
        // Given
        val headers = listOf("id", "name")
        val values = listOf("1", "Test Meal")

        // When
        val result = parser.parse(headers, values)

        // Then
        assertThat(result).isEqualTo(testMeal)
    }

    @Test
    fun `should return partial meal when some values are type invalid`() {
        // Given
        val headers = listOf("id", "name", "minutes", "nutrition")
        val values = listOf("1", "Test Meal", "invalid", "[400.5]")

        // When
        val result = parser.parse(headers, values)

        // Then
        assertThat(result.nutrition.calories).isEqualTo(400.5)
        assertThat(result.preparationTimeInMinutes).isEqualTo(0)
    }


    @Test
    fun `should return default id when id header is not provided`() {
        // Given
        val headers = listOf("name", "minutes", "contributor_id")
        val values = listOf("Spaghetti", "30", "101")

        // When
        val result = parser.parse(headers, values)

        // Then
        assertThat(result.id).isEqualTo(0)
    }

    @Test
    fun `should return empty name when name header is not provided`() {
        // Given
        val headers = listOf("id", "minutes", "contributor_id")
        val values = listOf("5", "30", "101")

        // When
        val result = parser.parse(headers, values)

        // Then
        assertThat(result.name).isEqualTo("")
    }

}


