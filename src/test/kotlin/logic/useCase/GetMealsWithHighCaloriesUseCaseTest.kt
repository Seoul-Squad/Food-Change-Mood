package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.utils.NoMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.GetMealsWithHighCaloriesUseCase

class GetMealsWithHighCaloriesUseCaseTest{
  private lateinit var mealRepository: MealRepository
  private lateinit var getMealsWithHighCaloriesUseCase: GetMealsWithHighCaloriesUseCase

  @BeforeEach
  fun setup() {
   mealRepository = mockk()
   getMealsWithHighCaloriesUseCase = GetMealsWithHighCaloriesUseCase(mealRepository)
  }

    @Test
    fun `should return all meals when all meals have calories greater than 700`() {

    //Given
    val meals = listOf(
        createMeal(1,800.0),
        createMeal(2,800.0),
        createMeal(3,800.0),
        createMeal(4,800.0),
        createMeal(5,800.0)
   )
   every { mealRepository.getAllMeals() } returns meals

    //When
    val result = getMealsWithHighCaloriesUseCase()

    //Then
    assertEquals(meals, result.getOrNull())
  }

    @Test
    fun `should return only meals with calories greater than 700 when list contains meals with low calories and high calories`() {

        //Given
        val meals = listOf(
            createMeal(1,800.0),
            createMeal(2,800.0),
            createMeal(3,800.0),
            createMeal(4,800.0),
            createMeal(5,800.0),


            createMeal(6,500.0),
            createMeal(7,400.0)
        )
        every { mealRepository.getAllMeals() } returns meals

        //When
        val result = getMealsWithHighCaloriesUseCase()

        //Then
        val highCaloriesMealsIds = listOf(1,2,3,4,5)
        val actualMealsIds = result.getOrNull()?.map { it.id }
        assertThat(highCaloriesMealsIds).containsAtLeastElementsIn(actualMealsIds)
    }

    @Test
    fun `should return failure exception with type NoMealsFoundException when the list of meals is empty`() {

        //Given
        every { mealRepository.getAllMeals() } returns emptyList()

        //When
        val result = getMealsWithHighCaloriesUseCase()

        //Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }

 }