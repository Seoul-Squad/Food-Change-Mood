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
import org.seoulsquad.logic.repository.MealRepository

class ExploreCountryMealsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var exploreCountryMealsUseCase: ExploreCountryMealsUseCase

    @BeforeEach
    fun setUp() {
        mealRepository = mockk(relaxed = true)
        exploreCountryMealsUseCase = ExploreCountryMealsUseCase(mealRepository)
    }

    @Test
    fun `should filter and return meals that contain the country name in name description or tags`()
    {
        // Given
        val countryName = "egypt"
        val meals= listOf(
            createMeal("egypt koshary", "egyptian koshary", listOf("60-minutes", "course")),
            createMeal("falafel", "falafel and mesakaa is favourable meals", listOf("egypt")),
            createMeal("shawarma", "egyptian shawarma :egypt meal very delicious", listOf("egypt")),
            createMeal("om alli", "egyptian sweets", listOf("egypt-15-minutes")),
            createMeal("pizza", null, listOf("italy, pizza")),
            createMeal("sushi", "japanese sushi", listOf("japan, sushi")),
            createMeal("tacos", "mexican tacos", listOf("mexico", "tacos")),
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = exploreCountryMealsUseCase(countryName)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.size).isEqualTo(4)
    }

    @Test
    fun `should throw NoMealsFoundException when no meal found in meal list`() {
        // Given
        val countryName = "egypt"
        val meals = emptyList<Meal>()
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = exploreCountryMealsUseCase(countryName)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
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
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(BlankInputException::class.java)
    }

}
