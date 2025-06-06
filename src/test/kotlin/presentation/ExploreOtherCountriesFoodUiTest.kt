package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.useCase.ExploreCountryMealsUseCase
import logic.utils.BlankInputException
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.presentation.ExploreOtherCountriesFoodUi
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer

class ExploreOtherCountriesFoodUiTest {
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var exploreCountryMealsUi: ExploreOtherCountriesFoodUi
    private lateinit var exploreCountryMealsUseCase: ExploreCountryMealsUseCase
    private val egyptianMeals= listOf(
        createMeal(1, "egypt koshary", "egyptian koshary", listOf("course")),
        createMeal(2, "falafel", "falafel", listOf("egypt")),
        createMeal(3, "shawarma", "egyptian shawarma", listOf("egypt")),
        createMeal(4, "om alli", "egyptian sweets", listOf("egypt")),
        createMeal(5, "om alli", "egyptian sweets", listOf("egypt")),
        createMeal(6, "om alli", "egyptian sweets", listOf("egypt")),
    )

    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        exploreCountryMealsUseCase = mockk(relaxed = true)
        exploreCountryMealsUi = ExploreOtherCountriesFoodUi(exploreCountryMealsUseCase, reader, viewer)
    }

    @Test
    fun `should show meals when user enters valid country name`() {
        // Given
        every { reader.readString() } returnsMany listOf("egypt", "0")
        val meals = egyptianMeals
        every { exploreCountryMealsUseCase("egypt") } returns Result.success(meals)

        // When
        exploreCountryMealsUi.exploreOtherCountriesFood()

        // Then
        verify {
            viewer.display(any())
        }
    }

    @Test
    fun `should throw BlankInputException when user enters blank country name`() {
        // Given
        every { reader.readString() } returnsMany listOf("", "0")
        every { exploreCountryMealsUseCase("") } returns Result.failure(BlankInputException())

        // When
        exploreCountryMealsUi.exploreOtherCountriesFood()

        // Then
        verify {
            viewer.display(any())
        }
    }
}



