package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.utils.NoMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.seoulsquad.logic.useCase.GetMealsWithHighCaloriesUseCase
import org.seoulsquad.presentation.MealsWithHighCaloriesUi
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption
import kotlin.test.Test

class MealsWithHighCaloriesUiTest {
    private lateinit var getMealsWithHighCaloriesUseCase: GetMealsWithHighCaloriesUseCase
    private lateinit var mealPrinter: MealPrinter
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var mealsWithHighCaloriesUi: MealsWithHighCaloriesUi

    @BeforeEach
    fun setup() {
        getMealsWithHighCaloriesUseCase = mockk(relaxed = true)
        mealPrinter = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        mealsWithHighCaloriesUi = MealsWithHighCaloriesUi(getMealsWithHighCaloriesUseCase, mealPrinter, viewer, reader)
    }

    private val meals = listOf(
        createMeal(1, 800.0), createMeal(2, 800.0), createMeal(3, 800.0)
    )

    @Test
    fun `should display a short meal then full meal when there is meals and user likes the first meal`() {
        //Given
        every { getMealsWithHighCaloriesUseCase() } returns Result.success(meals)
        every { reader.readInt() } returns SuggestionFeedbackOption.LIKE.ordinal

        //When
        mealsWithHighCaloriesUi.getMealsWithHighCalories()

        //Then
        verify { mealPrinter.printShortMeal(any()) }
        verify { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should display short meals till like one meal and then display full meal when dislike meals then like another meal`() {
        //Given
        every { getMealsWithHighCaloriesUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(
            SuggestionFeedbackOption.DISLIKE.ordinal,
            SuggestionFeedbackOption.LIKE.ordinal
        )
        //When
        mealsWithHighCaloriesUi.getMealsWithHighCalories()

        //Then
        verify(exactly = 2) { mealPrinter.printShortMeal(any()) }
        verify { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should display Invalid option message and display choices again when user enters number out of choices then like one meal`() {
        //Given
        val invalidInput = 5
        every { getMealsWithHighCaloriesUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(invalidInput, SuggestionFeedbackOption.LIKE.ordinal)

        //When
        mealsWithHighCaloriesUi.getMealsWithHighCalories()

        //Then
        val invalidInputMessage = "Invalid option"
        verify { viewer.display(invalidInputMessage) }
        verify { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should display Invalid option message and display choices again when user enters any invalid input then like one meal`() {
        //Given
        val invalidInput = null
        every { getMealsWithHighCaloriesUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(invalidInput, SuggestionFeedbackOption.LIKE.ordinal)

        //When
        mealsWithHighCaloriesUi.getMealsWithHighCalories()

        //Then
        val invalidInputMessage = "Invalid option"
        verify { viewer.display(invalidInputMessage) }
        verify { mealPrinter.printFullMeal(any()) }
    }


    @Test
    fun `should display We are out of meals for now! message when user dislike all meals`() {
        //Given
        every { getMealsWithHighCaloriesUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(
            SuggestionFeedbackOption.DISLIKE.ordinal,
            SuggestionFeedbackOption.DISLIKE.ordinal,
            SuggestionFeedbackOption.DISLIKE.ordinal
        )

        //When
        mealsWithHighCaloriesUi.getMealsWithHighCalories()

        //Then
        val outOfMealsMessage = "We are out of meals for now!"
        verify { viewer.display(outOfMealsMessage) }
    }

    @Test
    fun `should display exception message with type NoMealsFoundException when there is no meals to display`() {
        //Given
        every { getMealsWithHighCaloriesUseCase() } returns Result.failure(NoMealsFoundException())

        //When
        mealsWithHighCaloriesUi.getMealsWithHighCalories()

        //Then
        verify { viewer.display("Error: ${NoMealsFoundException().message}") }
    }

}