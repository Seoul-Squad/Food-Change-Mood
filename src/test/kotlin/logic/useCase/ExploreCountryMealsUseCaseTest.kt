package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.BlankInputException
import logic.utils.NoMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.seoulsquad.logic.repository.MealRepository

class ExploreCountryMealsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var exploreCountryMealsUseCase: ExploreCountryMealsUseCase
    private val countryName = "egypt"
    private val egyptianMeals= listOf(
        createMeal(1, "egypt koshary", "egyptian koshary", listOf("course")),
        createMeal(2, "falafel", "falafel", listOf("egypt")),
        createMeal(3, "shawarma", "egyptian shawarma", listOf("egypt")),
        createMeal(4, "om alli", "egyptian sweets", listOf("egypt")),
        createMeal(5, "om alli", "egyptian sweets", listOf("egypt")),
        createMeal(6, "om alli", "egyptian sweets", listOf("egypt")),
    )
    private val otherCountriesMeals= listOf(
        createMeal(15, "pizza", null, listOf("italy")),
        createMeal(16, "sushi", "japanese sushi", listOf("japan"))
    )

    @BeforeEach
    fun setUp() {
        mealRepository = mockk(relaxed = true)
        exploreCountryMealsUseCase = ExploreCountryMealsUseCase(mealRepository)
    }

    @Test
    fun `should return meals that contain the country name in name description or tags and respect the limit`() {
        // Given
        val meals = egyptianMeals + otherCountriesMeals
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = exploreCountryMealsUseCase(countryName, limit = 4)

        // Then
        val expectedEgyptMealsIds = setOf(1,2,3,4,5,6)
        val actualReturnedMealsIds = result.getOrNull()?.map { it.id }
        assertThat(expectedEgyptMealsIds).containsAtLeastElementsIn(actualReturnedMealsIds)
        assertThat(actualReturnedMealsIds?.size).isEqualTo(4)
    }

    @Test
    fun `should return meals that contain the country name in name description or tags`() {
        // Given
        val meals = egyptianMeals + otherCountriesMeals
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = exploreCountryMealsUseCase(countryName)

        // Then
        val expectedEgyptMealsIds = setOf(1,2,3,4,5,6)
        val actualReturnedMealsIds = result.getOrNull()?.map { it.id }
        assertThat(expectedEgyptMealsIds).containsAtLeastElementsIn(actualReturnedMealsIds)
    }


    @Test
    fun `should throw NoMealsFoundException when no meal found in meal list`() {
        // Given
        val meals = emptyList<Meal>()
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = exploreCountryMealsUseCase(countryName)

        // Then
        assertThrows<NoMealsFoundException> {
           result.getOrThrow()
        }
    }


    @Test
    fun `should throw NoMealsFoundException when no meals found by country name `() {
        // Given
        val meals = otherCountriesMeals
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = exploreCountryMealsUseCase(countryName)

        // Then
        assertThrows<NoMealsFoundException> {
            result.getOrThrow()
        }
    }

    @Test
    fun `should throw BlankInputException when country name is blank`(){
        // Given
        val countryName = "".trim()
        val meals = emptyList<Meal>()
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = exploreCountryMealsUseCase(countryName)

        // Then
        assertThrows<BlankInputException> {
            result.getOrThrow()
        }
    }

}
