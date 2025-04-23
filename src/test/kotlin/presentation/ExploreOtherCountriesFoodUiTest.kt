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
        val meals = listOf(
            createMeal("Koshary", "Famous Egyptian dish", listOf("egypt")),
            createMeal("Shawarma", "Delicious wrap", listOf("egypt"))
        )
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



