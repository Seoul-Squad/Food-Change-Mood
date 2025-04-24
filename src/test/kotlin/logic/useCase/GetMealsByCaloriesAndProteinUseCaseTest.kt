package logic.useCase

import common.createMeal
import io.mockk.every
import io.mockk.mockk
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.repository.MealRepository

class GetMealsByCaloriesAndProteinUseCaseTest {

    private lateinit var repository: MealRepository
    private lateinit var getMealsByCaloriesAndProteinUseCase: GetMealsByCaloriesAndProteinUseCase

    @BeforeEach
    fun setup() {
        repository = mockk()
        getMealsByCaloriesAndProteinUseCase = GetMealsByCaloriesAndProteinUseCase(repository)
    }

    @Test
    fun `returns meals that match the calories and protein criteria`() {
        // Given
        val meal1 = createMeal(id = 1, nutrition = createMeal(id = 1).nutrition.copy(calories = 500.0, protein = 25.0))
        val meal2 = createMeal(id = 2, nutrition = createMeal(id = 2).nutrition.copy(calories = 510.0, protein = 24.0))
        val allMeals = listOf(meal1, meal2)

        every { repository.getAllMeals() } returns allMeals

        // When
        val result = getMealsByCaloriesAndProteinUseCase.invoke(targetCalories = 500.0, targetProtein = 25.0)

        // Then
        assertEquals(2, result.size)
        assertTrue(result.containsAll(listOf(meal1, meal2)))
    }

    @Test
    fun `throws exception when no meals match the criteria`() {
        // Given
        val meal1 = createMeal(id = 1, nutrition = createMeal(id = 1).nutrition.copy(calories = 700.0, protein = 10.0))
        val meal2 = createMeal(id = 2, nutrition = createMeal(id = 2).nutrition.copy(calories = 800.0, protein = 5.0))
        val allMeals = listOf(meal1, meal2)

        every { repository.getAllMeals() } returns allMeals

        // When
        val exception = assertThrows(NoMealsFoundException::class.java) {
            getMealsByCaloriesAndProteinUseCase.invoke(targetCalories = 500.0, targetProtein = 25.0)
        }
        //Then
        assertEquals("No meals found", exception.message)
    }
}
