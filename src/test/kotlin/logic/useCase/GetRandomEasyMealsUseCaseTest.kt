package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.NoMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.seoulsquad.logic.repository.MealRepository
import java.util.stream.Stream
import kotlin.test.Test

class GetRandomEasyMealsUseCaseTest{
  private lateinit var mealRepository: MealRepository
  private lateinit var getRandomEasyMealsUseCase: GetRandomEasyMealsUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        getRandomEasyMealsUseCase = GetRandomEasyMealsUseCase(mealRepository)
    }

    @ParameterizedTest
    @ValueSource(ints = [4, 5, 6])
    fun `should return only the limit random easy meals when change the limit in usecase`(limit: Int) {

        //Given
        every { mealRepository.getAllMeals() } returns twelveEasyMeals

        //When
        val result = getRandomEasyMealsUseCase(limit = limit)

        //Then
        assertEquals(limit, result.getOrNull()?.size)
    }

    @Test
    fun `should return only 10 random easy meals that available in the list when the list contain more than 10 easy meals`() {

        //Given
        every { mealRepository.getAllMeals() } returns twelveEasyMeals

        //When
        val result = getRandomEasyMealsUseCase()

        //Then
        val easyMealsIds = listOf(1,2,3,4,5,6,7,8,9,10,11,12)
        val actualMealsIds = result.getOrNull()?.map { it.id }
        assertThat(easyMealsIds).containsAtLeastElementsIn(actualMealsIds)
    }

    @Test
    fun `should return only easy meals that available in the list when the list contain less than 10 available easy meals`() {

        //Given
        every { mealRepository.getAllMeals() } returns fiveEasyMealsAndTwoNotEasyMeals

        //When
        val result = getRandomEasyMealsUseCase()

        //Then
        val easyMealsIds = listOf(3,4,5,6,7)
        val actualMealsIds = result.getOrNull()?.map { it.id }
        assertThat(easyMealsIds).containsAtLeastElementsIn(actualMealsIds)
    }

    @Test
    fun `should return failure exception with type NoMealsFoundException when the list of meals is empty`() {

        //Given
        every { mealRepository.getAllMeals() } returns emptyList()

        //When
        val result = getRandomEasyMealsUseCase()

        //Then
        assertTrue(result.isFailure)
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @ParameterizedTest
    @MethodSource("getNotEasyMealsLists")
    fun `should return failure exception with type NoMealsFoundException when all meals violate any condition of being easy`(meals: List<Meal>) {

        //Given
        every { mealRepository.getAllMeals() } returns meals

        //When
        val result = getRandomEasyMealsUseCase()

        //Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }



    companion object{
        @JvmStatic
        fun getNotEasyMealsLists(): Stream<Arguments> =

            Stream.of(
                Arguments.argumentSet(
                    "list of meals with preparation time more than 30 minutes",
                    listOf(
                        createMeal(1,35, 4, 4),
                        createMeal(2,40, 4, 4),
                        createMeal(3,45, 4, 4)
                    )
                ),
                Arguments.argumentSet(
                    "list of meals with number of ingredients more than 5",
                    listOf(
                        createMeal(1,25, 8, 4),
                        createMeal(2,25, 9, 4),
                        createMeal(3,25, 7, 4)
                    )
                ),
                Arguments.argumentSet(
                    "list of meals with number of steps more than 6",
                    listOf(
                        createMeal(1,25, 4, 7),
                        createMeal(2,25, 4, 8),
                        createMeal(3,25, 4, 9)
                    )
                )
            )


        val twelveEasyMeals = listOf (
            createMeal(1, 25, 2, 5),
            createMeal(2, 15, 5, 3),
            createMeal(3, 18, 3, 4),
            createMeal(4, 28, 4, 5),
            createMeal(5, 30, 5, 6),
            createMeal(6, 25, 2, 5),
            createMeal(7, 15, 5, 3),
            createMeal(8, 18, 3, 4),
            createMeal(9, 28, 4, 5),
            createMeal(10,30, 5, 6),
            createMeal(11,30, 5, 6),
            createMeal(12,30, 5, 6),
        )

        val fiveEasyMealsAndTwoNotEasyMeals = listOf (
            createMeal(1, 35, 5, 6),
            createMeal(2, 30, 8, 6),

            createMeal(3,25, 2, 5),
            createMeal(4,15, 5, 3),
            createMeal(5,18, 3, 4),
            createMeal(6,28, 4, 5),
            createMeal(7,30, 5, 6),
        )
    }

 }