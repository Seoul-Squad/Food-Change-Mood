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
    fun `should show meals when not empty query input find match`() {
        // Given
        val query = "kushari"
        val exitCode = "0"
        val meals = kushariMeals
        every { reader.readString() } returnsMany listOf(query, exitCode)
        every { searchMealsByNameUseCase(query) } returns Result.success(meals)
        // When
        searchMealByNameUi.searchMealByName()
        // Then
        verify {
            viewer.display(any())
            viewer.display(any())
            reader.readString()
            viewer.display(any())
            mealPrinter.printSearchResult(meals)
        }
    }

    @Test
    fun `should return failure NoMealsException when not empty query input don't find match`() {
        // Given
        val query = "kushari"
        val exitCode = "0"
        every { reader.readString() } returnsMany listOf(query, exitCode)
        every { searchMealsByNameUseCase(query) } returns Result.failure(NoMealsFoundException())
        // When
        searchMealByNameUi.searchMealByName()
        // Then
        verify(exactly = 1) { viewer.display("Error: ${NoMealsFoundException().message}") }
    }

    @Test
    fun `should return failure BlankInputException when empty query`() {
        // Given
        val query = ""
        val exitCode = "0"
        every { reader.readString() } returnsMany listOf(query, exitCode)
        every { searchMealsByNameUseCase(query) } returns Result.failure(BlankInputException())
        // When
        searchMealByNameUi.searchMealByName()
        // Then
        verify(exactly = 1) { viewer.display("Error: ${BlankInputException().message}") }
    }

}