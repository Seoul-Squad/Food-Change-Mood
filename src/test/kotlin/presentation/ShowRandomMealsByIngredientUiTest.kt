package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.model.Meal
import logic.useCase.GetRandomMealsByIngredientUseCase
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.presentation.ShowRandomMealsByIngredientUi
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter

class ShowRandomMealsByIngredientUiTest {
    private lateinit var showRandomMealsByIngredientUi: ShowRandomMealsByIngredientUi
    private lateinit var getRandomMealsByIngredientUseCase: GetRandomMealsByIngredientUseCase
    private lateinit var mealPrinter: MealPrinter
    private lateinit var viewer: Viewer

    @BeforeEach
    fun setUp() {
        getRandomMealsByIngredientUseCase = mockk()
        mealPrinter = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        showRandomMealsByIngredientUi =
            ShowRandomMealsByIngredientUi(getRandomMealsByIngredientUseCase, mealPrinter, viewer)
    }

    @Test
    fun `should print random meals when use case returns success with the exact same limit`() {
        // Given
        val meals =
            List<Meal>(10) { mockk() }
        every { getRandomMealsByIngredientUseCase(any()) } returns Result.success(meals)

        // When
        showRandomMealsByIngredientUi.startShowRandomMealsByIngredient()

        // Then
        verify(exactly = 1) { viewer.display(any()) }
        verify(exactly = 10) { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should print random meals when use case returns success with less than the limit`() {
        // Given
        val meals = List<Meal>(4) { mockk() }
        every { getRandomMealsByIngredientUseCase(any()) } returns Result.success(meals)

        // When
        showRandomMealsByIngredientUi.startShowRandomMealsByIngredient()

        // Then
        verify(exactly = 2) { viewer.display(any()) }
        verify(exactly = 4) { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should print exception message when use case returns failure with NoMealsFoundException`() {
        // Given
        every { getRandomMealsByIngredientUseCase(any()) } returns Result.failure(NoMealsFoundException())

        // When
        showRandomMealsByIngredientUi.startShowRandomMealsByIngredient()

        // Then
        verify { viewer.display(any()) }
        verify(exactly = 0) { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should handle other exceptions properly when use case returns failure with different exception type`() {
        // Given
        val exception = RuntimeException("Some other error")
        every { getRandomMealsByIngredientUseCase(any()) } returns Result.failure(exception)

        // When
        showRandomMealsByIngredientUi.startShowRandomMealsByIngredient()

        // Then
        verify(exactly = 1) { viewer.display(any()) }
        verify(exactly = 0) { mealPrinter.printFullMeal(any()) }
    }
}
