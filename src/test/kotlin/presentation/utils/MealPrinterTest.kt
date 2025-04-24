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
    fun `printShortMeal should print meal name and description`() {
        val meal = createSampleMeal()
        printer.printShortMeal(meal)

        verify { viewer.display("\u001B[1mMeal: ${meal.name}\u001B[0m") }
        verify { viewer.display(meal.description!!) }
    }

    @Test
    fun `printFullMeal should print all meal details`() {
        val meal = createSampleMeal()
        printer.printFullMeal(meal)

        verify { viewer.display("Meal: ${meal.name} (ID: ${meal.id})") }
        verify { viewer.display("Time to Prepare: ${meal.preparationTimeInMinutes} minutes") }
        verify { viewer.display("Ingredients (${meal.numberOfIngredients}):") }
        meal.ingredients.forEachIndexed { index, ingredient ->
            verify { viewer.display("  ${index + 1}. $ingredient") }
        }
        verify { viewer.display("Steps (${meal.numberOfSteps}):") }
        meal.steps.forEachIndexed { index, step ->
            verify { viewer.display("  Step ${index + 1}: $step") }
        }
        verify { viewer.display("Nutrition:") }
        verify { viewer.display("  - Calories: ${meal.nutrition.calories} kcal") }
        verify { viewer.display("  - Total Fat: ${meal.nutrition.totalFat} g") }
        verify { viewer.display("  - Saturated Fat: ${meal.nutrition.saturatedFat} g") }
        verify { viewer.display("  - Sugar: ${meal.nutrition.sugar} g") }
        verify { viewer.display("  - Sodium: ${meal.nutrition.sodium} mg") }
        verify { viewer.display("  - Protein: ${meal.nutrition.protein} g") }
        verify { viewer.display("  - Carbohydrates: ${meal.nutrition.carbohydrates} g") }
    }

    @Test
    fun `printMeal should display simple meal info`() {
        val meal = createSampleMeal()
        printer.printMeal(meal)

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
    fun `printSearchResult should call printMeal for each meal`() {
        val meals = listOf(createSampleMeal(1), createSampleMeal(2), createSampleMeal(3))
        printer.printSearchResult(meals)

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
    fun `printLikeAndDislikeOptions should display all feedback options`() {
        SuggestionFeedbackOption.entries.forEach {
            verify(exactly = 0) { viewer.display("${it.ordinal}. ${it.title}") } // no call before
        }

        printer.printLikeAndDislikeOptions()

        SuggestionFeedbackOption.entries.forEach {
            verify { viewer.display("${it.ordinal}. ${it.title}") }
        }
    }


    @Test
    fun `printShortMeal should display name and description if description is present`() {
        val meal = createSampleMeal()

        printer.printShortMeal(meal)

        verify { viewer.display("\u001B[1mMeal: ${meal.name}\u001B[0m") }
        verify { viewer.display(meal.description!!) }
    }


    @Test
    fun `printShortMeal should display only name if description is null`() {
        val meal = createSampleMeal().copy(description = null)

        printer.printShortMeal(meal)

        verify { viewer.display("\u001B[1mMeal: ${meal.name}\u001B[0m") }
        verify(exactly = 0) { viewer.display(null as String?) }
    }


    @Test
    fun `printShortMeal should display only name if description is blank`() {
        val meal = createSampleMeal().copy(description = "  ")

        printer.printShortMeal(meal)

        verify { viewer.display("\u001B[1mMeal: ${meal.name}\u001B[0m") }
        verify(exactly = 0) { viewer.display("  ") }
    }


    @Test
    fun `printFullMeal should display name, id, preparation time and description when available`() {
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
    fun `printFullMeal should display name, id, preparation time but not description when it is null or blank`() {
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
    fun `printFullMeal should not display blank description`() {
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
