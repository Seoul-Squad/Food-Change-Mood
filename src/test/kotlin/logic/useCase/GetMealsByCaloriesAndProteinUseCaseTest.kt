package logic.useCase

import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.model.Nutrition
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.repository.MealRepository
import kotlinx.datetime.LocalDate


class GetMealsByCaloriesAndProteinUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: GetMealsByCaloriesAndProteinUseCase

    @BeforeEach
    fun setUp() {
        mealRepository = mockk()
        useCase = GetMealsByCaloriesAndProteinUseCase(mealRepository)
    }

    @Test
    fun `should return meals within calorie and protein margin`() {
        // Given
        val targetCalories = 200.0
        val targetProtein = 20.0

        every { mealRepository.getAllMeals() } returns listOf(
            createMeal("Match Meal", calories = 200.0, protein = 20.0),
            createMeal("Low Protein", calories = 200.0, protein = 10.0),
            createMeal("High Calorie", calories = 250.0, protein = 20.0)
        )

        // When
        val result = useCase(targetCalories, targetProtein, 10.0)

        // Then
        assertEquals(1, result.size)
        assertEquals("Match Meal", result.first().name)
    }

    @Test
    fun `should throw NoMealsFoundException when no meals match`() {
        // Given
        val targetCalories = 200.0
        val targetProtein = 20.0

        every { mealRepository.getAllMeals() } returns listOf(
            createMeal("No Match", calories = 300.0, protein = 30.0)
        )

        // Then
        assertThrows(NoMealsFoundException::class.java) {
            useCase(targetCalories, targetProtein,   )
        }
    }

    private fun createMeal(
        name: String,
        calories: Double,
        protein: Double,
    ): Meal {
        return Meal(
            name = name,
            id = 1,
            preparationTimeInMinutes = 10,
            contributorId = 123,
            submittedAt = LocalDate(2023, 1, 1),
            tags = listOf("tag1"),
            numberOfSteps = 2,
            steps = listOf("Step 1", "Step 2"),
            description = "test meal",
            ingredients = listOf("ing1", "ing2"),
            numberOfIngredients = 2,
            nutrition = Nutrition(
                calories = calories,
                protein = protein,
            ),
        )
    }
}
