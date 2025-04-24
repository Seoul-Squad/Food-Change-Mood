import common.createMeal
import common.createNutrition
import io.mockk.every
import io.mockk.mockk
import logic.useCase.GetMealsByCaloriesAndProteinUseCase
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.Test
import org.seoulsquad.presentation.MealsByCaloriesAndProteinUi
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue

class MealsByCaloriesAndProteinUiTest {

    private val mockUseCase = mockk<GetMealsByCaloriesAndProteinUseCase>()

    @Test
    fun `should print meal details when user enters valid calories and protein`() {

        val input = "250\n20\n" // 250 calories, 20 protein
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val expectedMeal = createMeal(
            id = 1,
            name = "Grilled Chicken",
            nutrition = createNutrition(calories = 250.0, protein = 20.0)
        )

        every {
            mockUseCase(250.0, 20.0)
        } returns listOf(expectedMeal)

        val ui = MealsByCaloriesAndProteinUi(mockUseCase)

        ui.startGetMealsByCaloriesAndProtein()

        val printedOutput = output.toString()
        assertTrue(printedOutput.contains("✅ Found 1 meal(s)"))
        assertTrue(printedOutput.contains("Grilled Chicken"))
    }

    @Test
    fun `should show error message when no meals are found`() {
        val input = "500\n50\n250\n20\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        every { mockUseCase(500.0, 50.0) } throws NoMealsFoundException()
        every { mockUseCase(250.0, 20.0) } returns listOf(
            createMeal(id = 2, name = "Healthy Wrap", nutrition = createNutrition(250.0, 20.0))
        )

        val ui = MealsByCaloriesAndProteinUi(mockUseCase)

        ui.startGetMealsByCaloriesAndProtein()

        val printed = output.toString()
        assertTrue(printed.contains("⚠️ No meals found"))
        assertTrue(printed.contains("✅ Found 1 meal(s)"))
        assertTrue(printed.contains("Healthy Wrap"))
    }
}
