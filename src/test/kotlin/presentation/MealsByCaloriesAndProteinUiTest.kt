import common.createMeal
import io.mockk.mockk
import logic.useCase.GetMealsByCaloriesAndProteinUseCase
import org.junit.jupiter.api.Test
import io.mockk.*
import org.seoulsquad.presentation.MealsByCaloriesAndProteinUi
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.junit.jupiter.api.BeforeEach
import logic.utils.NoMealsFoundException


class MealsByCaloriesAndProteinUiTest {

    private val getMealsByCaloriesAndProteinUseCase = mockk<GetMealsByCaloriesAndProteinUseCase>()
    private val viewer = mockk<Viewer>(relaxed = true) // Relaxed to skip unnecessary stubs
    private val reader = mockk<Reader>()
    private val ui = MealsByCaloriesAndProteinUi(getMealsByCaloriesAndProteinUseCase, viewer, reader)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `should quits immediately when user type 'quit' or 'q' or 'exit'`() {
        every { reader.readString() } returns "quit"

        ui.startGetMealsByCaloriesAndProtein()

        verify {
            viewer.display("(Enter 'quit' at any time to exit)")
            getMealsByCaloriesAndProteinUseCase wasNot Called
        }
    }

    @Test
    fun `should shows meals when calories and protein are positive number`() {
        val testMeal = createMeal(1,"Chicken Salad")
        every { reader.readString() } returnsMany listOf("500", "30", "quit")
        every { getMealsByCaloriesAndProteinUseCase(500.0, 30.0) } returns listOf(testMeal)

        ui.startGetMealsByCaloriesAndProtein()

        verify {
            viewer.display("✅ Found 1 meal(s) around ~500.0 kcal and ~30.0g protein:")
            viewer.display("- Chicken Salad")
        }
    }
    @Test
    fun `should continues loop when user presses Enter`() {
        val testMeal = createMeal(1,"Chicken Salad")
        every { reader.readString() } returnsMany listOf("500", "30", "", "quit")
        every { getMealsByCaloriesAndProteinUseCase(500.0, 30.0) } returns listOf(testMeal)

        ui.startGetMealsByCaloriesAndProtein()

        verify {
            viewer.display("✅ Found 1 meal(s) around ~500.0 kcal and ~30.0g protein:")
            viewer.display("- Chicken Salad")
            viewer.display("\nSearch again? (Press Enter to continue or 'quit' to exit): ")
        }////////==========
    }

    @Test
    fun `should shows error when user enter a character(s) or special character`() {
        every { reader.readString() } returnsMany listOf("300", "@DB", "quit")

        ui.startGetMealsByCaloriesAndProtein()

        verify {
            viewer.display("❌ Please enter a number")
        }
        verify(exactly = 0) {
            viewer.display("Enter target protein (g)")
        }
    }

    @Test
    fun `should show error when user enter 0 or negative number`() {
        every { reader.readString() } returnsMany listOf("0", "-20", "quit")

        ui.startGetMealsByCaloriesAndProtein()

        verify {
            viewer.display("❌ Please enter a positive number")
        }
        verify(exactly = 0) {
            viewer.display("Enter target calories")
            viewer.display("Enter target protein (g)")
        }
    }
    @Test
    fun `should throws NoMealsFoundException when no meals found `() {
        every { reader.readString() } returnsMany listOf("600", "40", "quit")
        every { getMealsByCaloriesAndProteinUseCase(600.0, 40.0) } throws NoMealsFoundException()

        ui.startGetMealsByCaloriesAndProtein()

        verify {
            viewer.display("⚠️ No meals found")
        }
    }
}
