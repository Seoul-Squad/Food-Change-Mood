package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.utils.BlankInputException
import logic.utils.NoMealsFoundException
import mockData.createSearchByNameMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.useCase.SearchMealsByNameUseCase
import org.seoulsquad.presentation.SearchMealByNameUi
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter

class SearchMealByNameUiTest {
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var mealPrinter: MealPrinter
    private lateinit var searchMealByNameUi: SearchMealByNameUi
    private lateinit var searchMealsByNameUseCase: SearchMealsByNameUseCase
    private val kushariMeals = listOf(
        createSearchByNameMeal("eastern kushari"),
        createSearchByNameMeal("delicious kushari"),
        createSearchByNameMeal("kushari kushari ")
    )

    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        mealPrinter = MealPrinter(viewer)
        searchMealsByNameUseCase = mockk(relaxed = true)
        searchMealByNameUi = SearchMealByNameUi(searchMealsByNameUseCase, reader, viewer, mealPrinter)
    }

    @Test
    fun `should show meals when user query valid and there is a match`() {
        // Given
        every { reader.readString() } returnsMany listOf("kushari", "0")
        val meals = kushariMeals
        every { searchMealsByNameUseCase("kushari") } returns Result.success(meals)
        // When
        searchMealByNameUi.searchMealByName()
        // Then
        verify {
            viewer.display(any())
        }
    }

    @Test
    fun `should throw NoMealsException when user enters query that is not matched`() {
        // Given
        every { reader.readString() } returnsMany listOf("kushari", "0")
        every { searchMealsByNameUseCase("kushari") } returns Result.failure(NoMealsFoundException())
        // When
        searchMealByNameUi.searchMealByName()
        // Then
        verify {
            viewer.display(any())
        }
    }

    @Test
    fun `should throw BlankInputException when user enters blank query`() {
        // Given
        every { reader.readString() } returnsMany listOf("", "0")
        every { searchMealsByNameUseCase("") } returns Result.failure(BlankInputException())
        // When
        searchMealByNameUi.searchMealByName()
        // Then
        verify {
            viewer.display(any())
        }
    }

}