package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.NoMealsFoundException
import mockData.createPotatoMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.seoulsquad.logic.repository.MealRepository
import java.util.stream.Stream

class GetRandomPotatoMealsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase

    @BeforeEach
    fun setUp() {
        mealRepository = mockk()
        getRandomPotatoMealsUseCase = GetRandomPotatoMealsUseCase(mealRepository)
    }

    @ParameterizedTest
    @MethodSource("provideMealsThatNotContainsPotato")
    fun `should return a failure with NoMealsFoundException when no meals contains potato in it's ingredients are available`(
        meals: List<Meal>,
    ) {
        // Given
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }

    @Test
    fun `should return a list of random meals that contains potato in it's ingredients when given a list of meals`() {
        // Given
        val meals =
            listOf(
                createPotatoMeal(1, listOf("meat", "potato", "rize")),
                createPotatoMeal(2, listOf("meat", "salamon", "rize")),
                createPotatoMeal(3, listOf("meat", "hot chocolate", "rize")),
                createPotatoMeal(4, listOf("meat", "frizes", "rize")),
                createPotatoMeal(5, listOf("potato", "potato", "rize")),
                createPotatoMeal(6, listOf("meat", "molokhiya", "potato")),
            )

        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getRandomPotatoMealsUseCase()
        // Then
        assertThat(result.getOrNull()?.map { it.id }).containsExactlyElementsIn(listOf(2, 5, 6))
    }

    @Test
    fun `should return a list of random meals that contains potato in it's ingredients when given a list of meals in any case`() {
        // Given
        val meals =
            listOf(
                createPotatoMeal(1, listOf("meat", "POTATO", "fool")),
                createPotatoMeal(2, listOf("meat", "salamon", "rize")),
                createPotatoMeal(3, listOf("meat", "hot chocolate", "PoTAtO")),
                createPotatoMeal(4, listOf("meat", "potato", "salmon")),
            )

        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(
            result
                .getOrNull()
                ?.map { it.ingredients }
                ?.flatten()
                ?.contains("potato"),
        ).isTrue()
        // assertThat(result.getOrNull()?.map { it.id }?.toSet()).isEqualTo(setOf(1, 3, 4))
    }

    @Test
    fun `should return a list of random meals that contains potato in it's ingredients within the limit when given list of meals more than the limit`() {
        // Given
        val meals =
            listOf(
                createPotatoMeal(1, listOf("meat", "POTATO", "fool")),
                createPotatoMeal(2, listOf("meat", "salamon", "rize")),
                createPotatoMeal(3, listOf("meat", "hot chocolate", "PoTAtO")),
                createPotatoMeal(4, listOf("meat", "potato", "salmon")),
                createPotatoMeal(5, listOf("meat", "potato", "rize")),
                createPotatoMeal(6, listOf("meat", "salamon", "rize")),
                createPotatoMeal(7, listOf("meat", "hot chocolate", "rize")),
                createPotatoMeal(8, listOf("meat", "frizes", "rize")),
                createPotatoMeal(9, listOf("potato", "potato", "rize")),
                createPotatoMeal(10, listOf("meat", "molokhiya", "potato")),
                createPotatoMeal(11, listOf("meat", "POTATO", "fool")),
                createPotatoMeal(12, listOf("meat", "salamon", "rize")),
                createPotatoMeal(13, listOf("meat", "hot chocolate", "PoTAtO")),
                createPotatoMeal(14, listOf("meat", "potato", "salmon")),
                createPotatoMeal(15, listOf("potato", "potato", "rize")),
                createPotatoMeal(16, listOf("potato", "potato", "rize")),
                createPotatoMeal(17, listOf("potato", "potato", "rize")),
            )
        every { mealRepository.getAllMeals() } returns meals

        val tenResult = getRandomPotatoMealsUseCase()
        assertThat(tenResult.getOrNull()?.size).isEqualTo(10)

        // this will fail
        val fiftyResults = getRandomPotatoMealsUseCase(15)
        assertThat(fiftyResults.getOrNull()?.size).isEqualTo(15)
    }

    companion object {
        @JvmStatic
        fun provideMealsThatNotContainsPotato(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    emptyList<Meal>(),
                ),
                Arguments.of(
                    listOf(
                        createPotatoMeal(1, listOf("meat", "salamon", "rize")),
                        createPotatoMeal(2, listOf("meat", "salamon", "rize")),
                        createPotatoMeal(3, listOf("meat", "salamon", "rize")),
                        createPotatoMeal(4, listOf("meat", "salamon", "rize")),
                        createPotatoMeal(5, listOf("meat", "salamon", "rize")),
                    ),
                ),
            )
    }
}
