package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.utils.Constants
import logic.utils.Constants.Ingredients.INGREDIENT_EGG
import logic.utils.Constants.Tags.TAG_SWEET
import logic.utils.NoMealsFoundException
import mockData.createMeal
import utils.createMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.seoulsquad.logic.repository.MealRepository
import kotlin.test.assertContains
import kotlin.test.assertContentEquals

class GetSweetsWithNoEggsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase

    @BeforeEach
    fun setUp() {
        mealRepository = mockk(relaxed = true)
        getSweetsWithNoEggsUseCase = GetSweetsWithNoEggsUseCase(mealRepository)
    }

    @Test
    fun `should return success result with list of sweets with no eggs when they are available`() {
        //Given
        val meals = listOf(
            createMeal(name = "Vegan cupcake", tags = listOf(TAG_SWEET), ingredients = listOf("flour", "sugar", "vegan butter")),
            createMeal(name = "Regular cupcake", tags = listOf(TAG_SWEET), ingredients = listOf("flour", "sugar", "butter", INGREDIENT_EGG)),
            createMeal(name = "Salad", tags = listOf("healthy"), ingredients = listOf("lettuce", "tomato", "cucumber"))
        )
        every { mealRepository.getAllMeals() } returns meals

        //When
        val result = getSweetsWithNoEggsUseCase()

        //Then
        assertContentEquals(result.getOrNull()?.map { it.name }, listOf("Vegan cupcake"))
    }

    @Test
    fun `should return failure result with NoMealsFoundException when there is no sweets without eggs`() {
        //Given
        val meals = listOf(
            createMeal(
                name = "Salad",
                tags = listOf("healthy"),
                ingredients = listOf("lettuce", "tomato", "cucumber")
            )
        )
        every { mealRepository.getAllMeals() } returns meals

        //When
        val result = getSweetsWithNoEggsUseCase()

        //Then
        assertThrows<NoMealsFoundException> {
            result.getOrThrow()
        }
    }

    @Test
    fun `should return failure result with NoMealsFoundException when there is only non-sweets with eggs`() {
        //Given
        val meals = listOf(
            createMeal(
                name = "Regular cupcake",
                tags = listOf(TAG_SWEET),
                ingredients = listOf("flour", "sugar", "butter", "eggs")
            )
        )
        every { mealRepository.getAllMeals() } returns meals

        //When
        val result = getSweetsWithNoEggsUseCase()

        //Then
        assertThrows<NoMealsFoundException> {
            result.getOrThrow()
        }
    }

    @Test
    fun `should return failure result with NoMealsFoundException when there is no meals`() {
        //Given
        every { mealRepository.getAllMeals() } returns emptyList()

        //When
        val result = getSweetsWithNoEggsUseCase()

        //Then
        assertThrows<NoMealsFoundException> {
            result.getOrThrow()
        }
    }
}