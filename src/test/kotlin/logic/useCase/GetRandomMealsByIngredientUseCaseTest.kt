package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.Constants.DEFAULT_INGREDIENT
import logic.utils.NoMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.seoulsquad.logic.repository.MealRepository
import java.util.stream.Stream

class GetRandomMealsByIngredientUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var getRandomMealsByIngredientUseCase: GetRandomMealsByIngredientUseCase
    private val ingredient = DEFAULT_INGREDIENT

    @BeforeEach
    fun setUp() {
        mealRepository = mockk()
        getRandomMealsByIngredientUseCase = GetRandomMealsByIngredientUseCase(mealRepository)
    }

    @ParameterizedTest
    @MethodSource("provideMealsThatNotContainsTheGivenIngredient")
    fun `should return a failure with NoMealsFoundException when no meal contains the given ingredient in the result`(meals: List<Meal>) {
        // Given
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getRandomMealsByIngredientUseCase(ingredient = DEFAULT_INGREDIENT)

        // Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }

    @ParameterizedTest
    @MethodSource("provideMealsThatContainsTheGivenIngredient")
    fun `should return meals that contains the given ingredient`(meals: List<Meal>) {
        // Given
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getRandomMealsByIngredientUseCase(ingredient)

        // Then
        val expectedMealsIds = listOf(1, 3, 4)
        val actualMealsIds = result.getOrNull()?.map { it.id }!!

        assertThat(expectedMealsIds).containsExactlyElementsIn(actualMealsIds)
    }

    @Test
    fun `should return meals that contains the given ingredient within the limit when there are more meals than the limit`() {
        // Given
        val meals =
            listOf(
                createMeal(1, listOf("meat", "POTATO", "fool")),
                createMeal(3, listOf("meat", "hot chocolate", "PoTAtO")),
                createMeal(4, listOf("meat", "potato", "salmon")),
                createMeal(5, listOf("meat", "potato", "rize")),
                createMeal(9, listOf("potato", "potato", "rize")),
                createMeal(11, listOf("meat", "POTATO", "fool")),
                createMeal(10, listOf("meat", "molokhiya", "potato")),
                createMeal(13, listOf("meat", "hot chocolate", "PoTAtO")),
                createMeal(2, listOf("meat", "salamon", "rize")),
                createMeal(6, listOf("meat", "salamon", "rize")),
                createMeal(7, listOf("meat", "hot chocolate", "rize")),
                createMeal(8, listOf("meat", "frizes", "rize")),
                createMeal(12, listOf("meat", "salamon", "rize")),
            )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getRandomMealsByIngredientUseCase(ingredient, 5)

        // Then
        assertThat(result.getOrNull()?.size).isAtMost(5)
    }

    companion object {
        @JvmStatic
        fun provideMealsThatNotContainsTheGivenIngredient(): Stream<Arguments> =
            Stream.of(
                Arguments.argumentSet(
                    "provide an empty list",
                    emptyList<Meal>(),
                ),
                Arguments.argumentSet(
                    "provide a list with out containing the given ingredient",
                    listOf(
                        createMeal(1, listOf("meat", "salamon", "rize")),
                        createMeal(2, listOf("meat", "salamon", "rize")),
                        createMeal(3, listOf("meat", "salamon", "rize")),
                        createMeal(4, listOf("meat", "salamon", "rize")),
                        createMeal(5, listOf("meat", "salamon", "rize")),
                    ),
                ),
            )

        @JvmStatic
        fun provideMealsThatContainsTheGivenIngredient(): Stream<Arguments> =
            Stream.of(
                Arguments.argumentSet(
                    "provide a list with containing the given ingredient",
                    listOf(
                        createMeal(1, listOf("meat", "potato", "rize")),
                        createMeal(2, listOf("meat", "salamon", "rize")),
                        createMeal(3, listOf("potato", "potato", "rize")),
                        createMeal(4, listOf("meat", "molokhiya", "potato")),
                    ),
                ),
                Arguments.argumentSet(
                    "provide a list with containing the given ingredient in different cases",
                    listOf(
                        createMeal(1, listOf("meat", "POTATO", "fool")),
                        createMeal(2, listOf("meat", "salamon", "rize")),
                        createMeal(3, listOf("meat", "hot chocolate", "PoTAtO")),
                        createMeal(4, listOf("meat", "potato", "salmon")),
                    ),
                ),
            )
    }
}
