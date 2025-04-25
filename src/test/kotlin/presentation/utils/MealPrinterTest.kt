package presentation.utils

import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.model.Nutrition
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption
import kotlin.test.Test

class MealPrinterTest{
    private val viewer = mockk<Viewer>(relaxed = true)
    private val printer = MealPrinter(viewer)

    private fun createSampleMeal(id: Int = 1): Meal {
        return Meal(
            name = "Sample Meal $id",
            id = id,
            preparationTimeInMinutes = 30,
            contributorId = 123,
            submittedAt = LocalDate(2024, 1, 1),
            tags = listOf("tag1", "tag2"),
            nutrition = Nutrition(
                calories = 400.0,
                totalFat = 25.0,
                sugar = 3.0,
                sodium = 300.0,
                protein = 20.0,
                saturatedFat = 5.0,
                carbohydrates = 10.0
            ),
            numberOfSteps = 2,
            steps = listOf("Step 1", "Step 2"),
            description = "A test description",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            numberOfIngredients = 2
        )
    }

    @Test
    fun `should return meal name and description when printShortMeal is called with valid description`() {
        // Given
        val meal = createSampleMeal()

        // When
        printer.printShortMeal(meal)

        // Then
        verify { viewer.display("\u001B[1mMeal: ${meal.name}\u001B[0m") }
        verify { viewer.display(meal.description!!) }
    }

    @Test
    fun `should return all meal details when printFullMeal is called`() {
        // Given
        val meal = createSampleMeal()

        // When
        printer.printFullMeal(meal)

        // Then
        verify { viewer.display(any()) }

        meal.ingredients.forEachIndexed { index, ingredient ->
            verify { viewer.display("  ${index + 1}. $ingredient") }
        }
        verify { viewer.display("Steps (${meal.numberOfSteps}):") }
        meal.steps.forEachIndexed { index, step ->
            verify { viewer.display("  Step ${index + 1}: $step") }
        }
        verify { viewer.display(any()) }

    }

    @Test
    fun `should return simplified meal view when printMeal is called`() {
        // Given
        val meal = createSampleMeal()

        // When
        printer.printMeal(meal)

        // Then
        verify {
            viewer.display(
                """
            -ID: ${meal.id}
                This recipe is called: ${meal.name},
            ${meal.description}

            ==============================================
            """.trimIndent()
            )
        }
    }

    @Test
    fun `should return meal printed individually when printSearchResult is called`() {
        // Given
        val meals = listOf(createSampleMeal(1), createSampleMeal(2), createSampleMeal(3))

        // When
        printer.printSearchResult(meals)

        // Then
        meals.forEach { meal ->
            verify {
                viewer.display(
                    match { displayedText ->
                        displayedText.contains("-ID: ${meal.id}") &&
                                displayedText.contains(meal.name)
                    }
                )
            }
        }
    }

    @Test
    fun `should return all feedback options when printLikeAndDislikeOptions is called`() {
        // Given (no call yet)
        SuggestionFeedbackOption.entries.forEach {
            verify(exactly = 0) { viewer.display("${it.ordinal}. ${it.title}") }
        }

        // When
        printer.printLikeAndDislikeOptions()

        // Then
        SuggestionFeedbackOption.entries.forEach {
            verify { viewer.display("${it.ordinal}. ${it.title}") }
        }
    }

    @Test
    fun `should return meal name and description when description is present in printShortMeal`() {
        // Given
        val meal = createSampleMeal()

        // When
        printer.printShortMeal(meal)

        // Then
        verify { viewer.display("\u001B[1mMeal: ${meal.name}\u001B[0m") }
        verify { viewer.display(meal.description!!) }
    }

    @Test
    fun `should return only meal name when description is null in printShortMeal`() {
        // Given
        val meal = createSampleMeal().copy(description = null)

        // When
        printer.printShortMeal(meal)

        // Then
        verify { viewer.display("\u001B[1mMeal: ${meal.name}\u001B[0m") }
        verify(exactly = 0) { viewer.display(null as String?) }
    }

    @Test
    fun `should return only meal name when description is blank in printShortMeal`() {
        // Given
        val meal = createSampleMeal().copy(description = "  ")

        // When
        printer.printShortMeal(meal)

        // Then
        verify { viewer.display("\u001B[1mMeal: ${meal.name}\u001B[0m") }
        verify(exactly = 0) { viewer.display("  ") }
    }

    @Test
    fun `should return name id time and description when description is present in printFullMeal`() {
        // Given
        val meal = createSampleMeal()

        // When
        printer.printFullMeal(meal)

        // Then
        verify { viewer.display("Meal: ${meal.name} (ID: ${meal.id})") }
        verify { viewer.display("Time to Prepare: ${meal.preparationTimeInMinutes} minutes") }
        verify { viewer.display(meal.description!!) }
    }

    @Test
    fun `should return name id and time only when description is null or blank in printFullMeal`() {
        // Given
        val meal = createSampleMeal().copy(description = null)

        // When
        printer.printFullMeal(meal)

        // Then
        verify { viewer.display("Meal: ${meal.name} (ID: ${meal.id})") }
        verify { viewer.display("Time to Prepare: ${meal.preparationTimeInMinutes} minutes") }
        verify(exactly = 0) { viewer.display(null as String?) }
    }

    @Test
    fun `should not display description when it is blank in printFullMeal`() {
        // Given
        val meal = createSampleMeal().copy(description = "  ")

        // When
        printer.printFullMeal(meal)

        // Then
        verify { viewer.display("Meal: ${meal.name} (ID: ${meal.id})") }
        verify { viewer.display("Time to Prepare: ${meal.preparationTimeInMinutes} minutes") }
        verify(exactly = 0) { viewer.display("  ") }
    }


}
