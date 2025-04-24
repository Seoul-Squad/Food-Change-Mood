package logic.useCase

import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.Constants.Ingredients.INGREDIENT_EGG
import logic.utils.Constants.Tags.TAG_SWEET
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.seoulsquad.logic.repository.MealRepository
import utils.createMeal
import java.util.stream.Stream
import kotlin.test.assertContentEquals

class GetSweetsWithNoEggsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase

    @BeforeEach
    fun setUp() {
        mealRepository = mockk(relaxed = true)
        getSweetsWithNoEggsUseCase = GetSweetsWithNoEggsUseCase(mealRepository)
    }

    @ParameterizedTest
    @MethodSource("provideSuccessScenarios")
    fun `success scenarios`(
        meals: List<Meal>, expectedIds: List<Int>
    ) {
        //Given
        every { mealRepository.getAllMeals() } returns meals

        //When
        val actualIds = getSweetsWithNoEggsUseCase().getOrNull()?.map { it.id }

        //Then
        assertContentEquals(actualIds, expectedIds)
    }

    @ParameterizedTest
    @MethodSource("provideFailureScenarios")
    fun `failure scenarios`(
        meals: List<Meal>
    ) {
        //Given
        every { mealRepository.getAllMeals() } returns meals

        //When
        val result = getSweetsWithNoEggsUseCase()

        //Then
        assertThrows<NoMealsFoundException> {
            result.getOrThrow()
        }
    }

    companion object {
        @JvmStatic
        fun provideSuccessScenarios(): Stream<Arguments> = Stream.of(
            Arguments.argumentSet(
                "should return success result with list of sweets with no eggs when they are available",
                listOf(
                    createMeal(
                        id = 1,
                        name = "Vegan cupcake",
                        tags = listOf(TAG_SWEET),
                        ingredients = listOf("flour", "sugar", "vegan butter")
                    ), createMeal(
                        id = 2,
                        name = "Regular cupcake",
                        tags = listOf(TAG_SWEET),
                        ingredients = listOf("flour", "sugar", "butter", INGREDIENT_EGG)
                    ), createMeal(
                        id = 3,
                        name = "Salad",
                        tags = listOf("healthy"),
                        ingredients = listOf("lettuce", "tomato", "cucumber")
                    )
                ),
                listOf(1)
            )
        )

        @JvmStatic
        fun provideFailureScenarios(): Stream<Arguments> = Stream.of(
            Arguments.argumentSet(
                "should return failure result with NoMealsFoundException when there is only sweets with eggs",
                listOf(
                    createMeal(
                        id = 1,
                        name = "Regular cupcake",
                        tags = listOf(TAG_SWEET),
                        ingredients = listOf("flour", "sugar", "butter", INGREDIENT_EGG)
                    ), createMeal(
                        id = 2,
                        name = "Salad",
                        tags = listOf("healthy"),
                        ingredients = listOf("lettuce", "tomato", "cucumber")
                    )
                )
            ),
            Arguments.argumentSet(
                "should return failure result with NoMealsFoundException when there is only non-sweets without eggs",
                listOf(
                    createMeal(
                        id = 1,
                        name = "Salad",
                        tags = listOf("healthy"),
                        ingredients = listOf("lettuce", "tomato", "cucumber")
                    ), createMeal(
                        id = 2,
                        name = "Salad",
                        tags = listOf("healthy"),
                        ingredients = listOf("lettuce", "tomato", "cucumber")
                    )
                )
            ),
            Arguments.argumentSet(
                "should return failure result with NoMealsFoundException when there is only non-sweets with eggs",
                listOf(
                    createMeal(
                        id = 1,
                        name = "Omelette",
                        tags = listOf("breakfast"),
                        ingredients = listOf("egg", "olive oil", "salt")
                    )
                )
            ),
            Arguments.argumentSet(
                "should return failure result with NoMealsFoundException when there is no meals",
                emptyList<Meal>()
            ),
        )
    }
}