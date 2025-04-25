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
    fun `should returns meals when they exact the same or close to given calories and protein`() {
        // Given
        val firstMeal = createMeal(id = 1, nutrition = createMeal(id = 1).nutrition.copy(calories = 500.0, protein = 25.0))
        val secondMeal = createMeal(id = 2, nutrition = createMeal(id = 2).nutrition.copy(calories = 510.0, protein = 24.0))
        val allMeals = listOf(firstMeal, secondMeal)

        every { repository.getAllMeals() } returns allMeals

        // When
        val result = getMealsByCaloriesAndProteinUseCase.invoke(targetCalories = 520.0, targetProtein = 26.0)

        // Then
        assertEquals(2, result.size)
        assertTrue(result.containsAll(listOf(firstMeal, secondMeal)))
    }

    @Test
    fun `should throws NoMealsFoundException when there isn't meals to the exact or close to the given calories and protein `() {
        // Given
        val firstMeal = createMeal(id = 1, nutrition = createMeal(id = 1).nutrition.copy(calories = 700.0, protein = 10.0))
        val secondMeal = createMeal(id = 2, nutrition = createMeal(id = 2).nutrition.copy(calories = 800.0, protein = 5.0))
        val allMeals = listOf(firstMeal, secondMeal)

        every { repository.getAllMeals() } returns allMeals

        // When
        val exception = assertThrows(NoMealsFoundException::class.java) {
            getMealsByCaloriesAndProteinUseCase.invoke(targetCalories = 500.0, targetProtein = 25.0)
        }
        //Then
        assertEquals("No meals found", exception.message)
    }

    @Test
    fun `should throws NoMealsFoundException when there isn't meals to the exact or close to the given protein`() {
        // Given
        val firstMeal = createMeal(id = 1, nutrition = createMeal(id = 1).nutrition.copy(calories = 700.0, protein = 10.0))
        val secondMeal = createMeal(id = 2, nutrition = createMeal(id = 2).nutrition.copy(calories = 800.0, protein = 5.0))
        val allMeals = listOf(firstMeal, secondMeal)

        every { repository.getAllMeals() } returns allMeals

        // When
        val exception = assertThrows(NoMealsFoundException::class.java) {
            getMealsByCaloriesAndProteinUseCase.invoke(targetCalories = 710.0, targetProtein = 25.0)
        }
        //Then
        assertEquals("No meals found", exception.message)
    }
    @Test
    fun `should throws NoMealsFoundException when there isn't meals for the given calories`() {
        // Given
        val firstMeal = createMeal(id = 1, nutrition = createMeal(id = 1).nutrition.copy(calories = 700.0, protein = 10.0))
        val secondMeal = createMeal(id = 2, nutrition = createMeal(id = 2).nutrition.copy(calories = 800.0, protein = 5.0))
        val allMeals = listOf(firstMeal, secondMeal)

        every { repository.getAllMeals() } returns allMeals

        // When
        val exception = assertThrows(NoMealsFoundException::class.java) {
            getMealsByCaloriesAndProteinUseCase.invoke(targetCalories = 1100.0, targetProtein = 5.5)
        }
        //Then
        assertEquals("No meals found", exception.message)
    }
}
