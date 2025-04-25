package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.utils.Constants.Tags.TAG_SWEET
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.presentation.SweetsWithNoEggsUi
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption
import utils.createMeal

class SweetsWithNoEggsUiTest {
    private lateinit var getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase
    private lateinit var mealPrinter: MealPrinter
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var sweetsWithNoEggsUi: SweetsWithNoEggsUi

    private val meals = listOf(
        createMeal(
            id = 1,
            name = "Vegan cupcake",
            tags = listOf(TAG_SWEET),
            ingredients = listOf("flour", "sugar", "vegan butter")
        ),
        createMeal(
            id = 2, name = "Ice cream", tags = listOf(TAG_SWEET), ingredients = listOf("cream", "vanilla", "ice")
        ),
    )

    @BeforeEach
    fun setup() {
        getSweetsWithNoEggsUseCase = mockk(relaxed = true)
        mealPrinter = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        sweetsWithNoEggsUi = SweetsWithNoEggsUi(
            getSweetsWithNoEggsUseCase = getSweetsWithNoEggsUseCase,
            mealPrinter = mealPrinter,
            viewer = viewer,
            reader
        )
    }

    @Test
    fun `should print a short meal then full meal when there is meals and user likes the first meal`() {
        //Given
        every { getSweetsWithNoEggsUseCase() } returns Result.success(meals)
        every { reader.readInt() } returns SuggestionFeedbackOption.LIKE.ordinal

        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 1) { mealPrinter.printShortMeal(any()) }
        verify(exactly = 1) { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should display error message when user enters invalid number suggestion feedback`() {
        //Given
        val invalidFeedback = 2
        every { getSweetsWithNoEggsUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(invalidFeedback, SuggestionFeedbackOption.LIKE.ordinal)

        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 1) { viewer.display(SweetsWithNoEggsUi.INVALID_OPTION_MESSAGE) }
    }

    @Test
    fun `should display error message when user enters invalid non-number suggestion feedback`() {
        //Given
        val invalidFeedback = null
        every { getSweetsWithNoEggsUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(invalidFeedback, SuggestionFeedbackOption.LIKE.ordinal)

        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 1) { viewer.display(SweetsWithNoEggsUi.INVALID_OPTION_MESSAGE) }
    }

    @Test
    fun `should print multiple short meals and then full meal when user dislikes first meal and likes the second meal`() {
        //Given
        every { getSweetsWithNoEggsUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(SuggestionFeedbackOption.DISLIKE.ordinal, SuggestionFeedbackOption.LIKE.ordinal)
        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 2) { mealPrinter.printShortMeal(any()) }
        verify(exactly = 1) { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should print out of meals message when user doesn't like any meals`() {
        //Given
        every { getSweetsWithNoEggsUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(SuggestionFeedbackOption.DISLIKE.ordinal, SuggestionFeedbackOption.DISLIKE.ordinal)

        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 1) { viewer.display(SweetsWithNoEggsUi.OUT_OF_MEALS_MESSAGE) }
    }

    @Test
    fun `should display error message when there is failure result with NoMealsFoundException`() {
        //Given
        every { getSweetsWithNoEggsUseCase() } returns Result.failure(NoMealsFoundException())

        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 1) { viewer.display(NoMealsFoundException().message) }
    }
}