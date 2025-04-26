package logic.useCase


import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.Constants.ITALIAN_NAME
import logic.utils.Constants.LARGE_GROUP_NAME
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.seoulsquad.logic.repository.MealRepository
import testData.createItalianLargeMeal
import java.util.stream.Stream

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
        every { mealRepository.getAllMeals() } returns emptyList()
        //When
        val result = getItalianLargeMealsUseCase()
        //Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMealScenarios")
    fun `GetItalianLargeMealsUseCase should return failure when no valid large Italian meals are found`(meals: List<Meal>) {
        //Given
        every { mealRepository.getAllMeals() } returns meals
        //When
        val result = getItalianLargeMealsUseCase()
        //Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }

    @ParameterizedTest
    @MethodSource("provideItalianLargeMealScenarios")
    fun `GetItalianLargeMealsUseCase should return the expected meals when large italian meal found`(
        meals: List<Meal>,
        expectedIds: List<Int>,
    ) {
        //Given
        every { mealRepository.getAllMeals() } returns meals
        //When
        val result = getItalianLargeMealsUseCase()
        //Then
        assertThat(result.isSuccess).isTrue()
        val actualIds = result.getOrNull()?.map { it.id }
        assertThat(actualIds?.all { it in expectedIds }).isTrue()
    }

    companion object {
        @JvmStatic
        fun provideInvalidMealScenarios(): Stream<Arguments> = Stream.of(
            Arguments.argumentSet(
                "No large Italian meals",
                listOf(
                    createItalianLargeMeal(listOf("carbo", "seafood"), "italian seafood"),
                    createItalianLargeMeal(listOf("milk", "banana"), "italian drink"),
                    createItalianLargeMeal(listOf("mocca", "lime"), null),
                    createItalianLargeMeal(listOf("meat", "bread"), "american plate")
                ),
            ),
            Arguments.argumentSet(
                "Only large non-Italian",
                listOf(
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "lime"), "milano drink"),
                    createItalianLargeMeal(listOf("meat", LARGE_GROUP_NAME), "american plate"),
                    createItalianLargeMeal(listOf("berry", LARGE_GROUP_NAME, "milk", "banana"), "sweden drink"),
                    createItalianLargeMeal(listOf("butter", "bread", "oil", LARGE_GROUP_NAME), "american plate")
                ),
            ),
            Arguments.argumentSet(
                " Only tagged Italian non-large",
                listOf(
                    createItalianLargeMeal(listOf(ITALIAN_NAME, "lime"), "milano drink"),
                    createItalianLargeMeal(listOf("meat", ITALIAN_NAME), "american plate"),
                    createItalianLargeMeal(listOf("berry", ITALIAN_NAME, "milk", "banana"), " drink"),
                    createItalianLargeMeal(listOf("butter", "bread", "oil", ITALIAN_NAME), "american plate")
                ),
            ),
            Arguments.argumentSet(
                "Only Italian in description non-large",
                listOf(
                    createItalianLargeMeal(listOf("carbo", "seafood"), "greek italian seafood"),
                    createItalianLargeMeal(listOf("milk", "banana"), "french italian drink"),
                    createItalianLargeMeal(listOf("orange", "lime"), "milano drink italian"),
                    createItalianLargeMeal(listOf("meat", "macaroni"), "italian american plate")
                )
            )
        )

        @JvmStatic
        fun provideItalianLargeMealScenarios(): Stream<Arguments> = Stream.of(
            Arguments.argumentSet(
                "large Italian meals",
                listOf(
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, ITALIAN_NAME), "greek italian seafood", 1),
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "milk", "banana"), "french italian drink", 2),
                    createItalianLargeMeal(listOf("orange", LARGE_GROUP_NAME, ITALIAN_NAME, "lime"), "milano drink", 3),
                    createItalianLargeMeal(
                        listOf(LARGE_GROUP_NAME, "meat", "macaroni", ITALIAN_NAME),
                        "italian american plate",
                        4
                    ),
                    createItalianLargeMeal(
                        listOf("large-group", "meat", "macaroni", "italiano"),
                        "italiano american plate",
                        5
                    ),
                ),
                listOf(1, 2, 3, 4),
            ),
            Arguments.argumentSet(
                "large tagged-Italian",
                listOf(
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, ITALIAN_NAME), "greek milano seafood", 1),
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "milk", "banana"), "french fruit drink", 2),
                    createItalianLargeMeal(listOf("orange", LARGE_GROUP_NAME, ITALIAN_NAME, "lime"), "milano drink", 3),
                    createItalianLargeMeal(
                        listOf(LARGE_GROUP_NAME, "meat", "macaroni", ITALIAN_NAME),
                        "mixed culture american plate",
                        4
                    ),
                    createItalianLargeMeal(
                        listOf("large-group", "meat", "macaroni", "souse"),
                        "burger american plate",
                        5
                    ),
                ),
                listOf(1, 3, 4),
            ),
            Arguments.argumentSet(
                "large described-Italian",
                listOf(
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "fish"), "greek italiano seafood", 1),
                    createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "milk", "banana"), "french italian drink", 2),
                    createItalianLargeMeal(listOf("orange", LARGE_GROUP_NAME, "lime"), "milano drink", 3),
                    createItalianLargeMeal(
                        listOf(LARGE_GROUP_NAME, "meat", "macaroni", "american"),
                        "italian american plate",
                        4
                    ),
                    createItalianLargeMeal(
                        listOf("large-group", "meat", "macaroni", "italiano"),
                        "italiano american plate",
                        5
                    ),
                ),
                listOf(1, 2, 4),

                ),
        )
    }


}