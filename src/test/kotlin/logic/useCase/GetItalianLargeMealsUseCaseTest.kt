package logic.useCase

import GetItalianLargeMealsUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.Constants.ITALIAN_NAME
import logic.utils.Constants.LARGE_GROUP_NAME
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.seoulsquad.logic.repository.MealRepository
import testData.createItalianLargeMeal
import java.util.stream.Stream
import kotlin.test.assertEquals

class GetItalianLargeMealsUseCaseTest {
    private lateinit var getItalianLargeMealsUseCase: GetItalianLargeMealsUseCase
    private lateinit var mealRepository: MealRepository

    @BeforeEach
    fun setup() {
        mealRepository = mockk(relaxed = true)
        getItalianLargeMealsUseCase = GetItalianLargeMealsUseCase(mealRepository)
    }

    @Test
    fun `GetItalianLargeMealsUseCase should return failure when no meals in repo`() {
        //Given
        every { mealRepository.getAllMeals() } returns listOf()
        //When
        val result = getItalianLargeMealsUseCase()
        //Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMealScenarios")
    fun `GetItalianLargeMealsUseCase should return failure when no valid large Italian meals are found`(meals: List<Meal>) {
        //Given
        every { mealRepository.getAllMeals() } returns meals
        //When
        val result = getItalianLargeMealsUseCase()
        //Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }


    @ParameterizedTest
    @MethodSource("provideValidMealScenarios")
    fun `GetItalianLargeMealsUseCase should return the expected meals when  large italian meal found`(
        meals: List<Meal>,
        expected: Int
    ) {
        //Given
        every { mealRepository.getAllMeals() } returns meals
        //When
        val result = getItalianLargeMealsUseCase()
        //Then
        assertThat(result.isSuccess).isTrue()
        assertEquals(expected, result.getOrNull()?.size)
    }
    companion object {
        @JvmStatic
        fun provideInvalidMealScenarios(): Stream<Arguments> = Stream.of(
            // Case 1: No large Italian meals
            Arguments.of(
                listOf(
                    createItalianLargeMeal(listOf("carbo", "seafood"), "italian seafood"),
                    createItalianLargeMeal(listOf("milk", "banana"), "italian drink"),
                    createItalianLargeMeal(listOf("mocca", "lime"), null),
                    createItalianLargeMeal(listOf("meat", "bread"), "american plate")
                ),
            ),

            // Case 2: Only large non-Italian
            Arguments.of(
                listOf(
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "lime"), "milano drink"),
                    createItalianLargeMeal(listOf("meat", LARGE_GROUP_NAME), "american plate"),
                    createItalianLargeMeal(listOf("berry", LARGE_GROUP_NAME, "milk", "banana"), "sweden drink"),
                    createItalianLargeMeal(listOf("butter", "bread", "oil", LARGE_GROUP_NAME), "american plate")
                ),
            ),

            // Case 3: Only tagged Italian non-large
            Arguments.of(
                listOf(
                    createItalianLargeMeal(listOf(ITALIAN_NAME, "lime"), "milano drink"),
                    createItalianLargeMeal(listOf("meat", ITALIAN_NAME), "american plate"),
                    createItalianLargeMeal(listOf("berry", ITALIAN_NAME, "milk", "banana"), "sweden drink"),
                    createItalianLargeMeal(listOf("butter", "bread", "oil", ITALIAN_NAME), "american plate")
                ),
            ),

            // Case 4: Only Italian in description non-large
            Arguments.of(
                listOf(
                    createItalianLargeMeal(listOf("carbo", "seafood"), "greek italian seafood"),
                    createItalianLargeMeal(listOf("milk", "banana"), "french italian drink"),
                    createItalianLargeMeal(listOf("orange", "lime"), "milano drink italian"),
                    createItalianLargeMeal(listOf("meat", "macaroni"), "italian american plate")
                )
            )
        )

        @JvmStatic
        fun provideValidMealScenarios(): Stream<Arguments> = Stream.of(
            // Case 1: large Italian meals, expected :
            Arguments.of(
                listOf(
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, ITALIAN_NAME), "greek italian seafood"),
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "milk", "banana"), "french italian drink"),
                    createItalianLargeMeal(listOf("orange", LARGE_GROUP_NAME, ITALIAN_NAME, "lime"), "milano drink"),
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "meat", "macaroni", ITALIAN_NAME), "italian american plate"),
                    createItalianLargeMeal(listOf("large-group", "meat", "macaroni", "italiano"), "italiano american plate"),
                ),
                4
            ),
            // Case 2:  large tagged-Italian
            Arguments.of(
                listOf(
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, ITALIAN_NAME), "greek italiano seafood"),
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "milk", "banana"), "french fruit drink"),
                    createItalianLargeMeal(listOf("orange", LARGE_GROUP_NAME, ITALIAN_NAME, "lime"), "milano drink"),
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "meat", "macaroni", ITALIAN_NAME), "mixed culture american plate"),
                    createItalianLargeMeal(listOf("large-group", "meat", "macaroni", "souse"), "burger american plate"),
                ),
                3
            ),
            // Case 3: large described-Italian
            Arguments.of(
                listOf(
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "fish"), "greek italian seafood"),
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "milk", "banana"), "french italian drink"),
                    createItalianLargeMeal(listOf("orange", LARGE_GROUP_NAME, "lime"), "milano drink"),
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "meat", "macaroni", "american"), "italian american plate"),
                    createItalianLargeMeal(listOf("large-group", "meat", "macaroni", "italiano"), "italiano american plate"),
                ),
                3
            ),
        )
    }


}