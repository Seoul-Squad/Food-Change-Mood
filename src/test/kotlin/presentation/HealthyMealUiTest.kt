package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.model.Meal
import logic.model.Nutrition
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.useCase.GetHealthyFastFoodUseCase
import org.seoulsquad.presentation.HealthyMealUi
import org.seoulsquad.presentation.consolelIO.Viewer

class HealthyMealUiTest {

    private lateinit var viewer: Viewer
    private lateinit var getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase
    private lateinit var healthyMealUi: HealthyMealUi

    private val healthyMeals = listOf(
        createMeal("Grilled Chicken Wrap", totalFat = 10.0, satFat = 2.0, carbs = 30.0, prepTime = 10),
        createMeal("Veggie Salad", totalFat = 5.0, satFat = 1.0, carbs = 15.0, prepTime = 5),
        createMeal("Quinoa Bowl", totalFat = 7.0, satFat = 1.5, carbs = 20.0, prepTime = 8)
    )

    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        getHealthyFastFoodUseCase = mockk()
        healthyMealUi = HealthyMealUi(getHealthyFastFoodUseCase, viewer)
    }

    @Test
    fun `should show greeting and meals when healthy meals are fetched successfully`() {
        // Given
        every { getHealthyFastFoodUseCase.getFastHealthyMeals() } returns Result.success(healthyMeals)

        // When
        healthyMealUi.presentHealthyMeal()

        // Then
        verify {
            viewer.display(any())
        }
    }

    @Test
    fun `should show error message when meal fetching fails`() {
        // Given
        val exception = RuntimeException("Something went wrong")
        every { getHealthyFastFoodUseCase.getFastHealthyMeals() } returns Result.failure(exception)

        // When
        healthyMealUi.presentHealthyMeal()

        // Then
        verify {
            viewer.display(any())
        }
    }

    @Test
    fun `should show unknown error when meal fetching fails with null message`() {
        // Given
        val exception = RuntimeException(null as String?)
        every { getHealthyFastFoodUseCase.getFastHealthyMeals() } returns Result.failure(exception)

        // When
        healthyMealUi.presentHealthyMeal()

        // Then
        verify {
            viewer.display(any())
        }
    }
}