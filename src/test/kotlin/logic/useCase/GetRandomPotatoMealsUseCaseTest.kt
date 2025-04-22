package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.utils.NoMealsFoundException
import mockData.createPotatoMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.repository.MealRepository

class GetRandomPotatoMealsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase

    @BeforeEach
    fun setUp() {
        mealRepository = mockk()
        getRandomPotatoMealsUseCase = GetRandomPotatoMealsUseCase(mealRepository)
    }

    @Test
    fun `should return a failure with NoMealsFoundException when no potato meals are available`() {
        // Given
        every { mealRepository.getAllMeals() } returns emptyList()

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return a list of random potato meals when given meals contains potato in it's ingredients`() {
        // Given
        val meals =
            listOf(
                createPotatoMeal(listOf("meat", "potato", "rize")),
                createPotatoMeal(listOf("meat", "salamon", "rize")),
                createPotatoMeal(listOf("meat", "hot chocolate", "rize")),
                createPotatoMeal(listOf("meat", "frizes", "rize")),
                createPotatoMeal(listOf("potato", "potato", "rize")),
                createPotatoMeal(listOf("meat", "molokhiya", "potato")),
            )

        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        val expectedListSize = 3
        assertThat(result.getOrNull()?.size).isEqualTo(expectedListSize)
    }

    @Test
    fun `should return a list of random potato meals when given meals contains in it's ingredients potato word in any case`() {
        // Given
        val meals =
            listOf(
                createPotatoMeal(listOf("meat", "POTATO", "fool")),
                createPotatoMeal(listOf("meat", "salamon", "rize")),
                createPotatoMeal(listOf("meat", "hot chocolate", "PoTAtO")),
                createPotatoMeal(listOf("meat", "potato", "salmon")),
            )

        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        val expectedListSize = 3
        assertThat(result.getOrNull()?.size).isEqualTo(expectedListSize)
    }
}
