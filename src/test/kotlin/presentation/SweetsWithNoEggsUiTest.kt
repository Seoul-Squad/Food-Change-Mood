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
import utils.createMeal

class SweetsWithNoEggsUiTest {
    private lateinit var getSweetsWithNoEggsUseCase: GetSweetsWithNoEggsUseCase
    private lateinit var mealPrinter: MealPrinter
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var sweetsWithNoEggsUi: SweetsWithNoEggsUi

    @BeforeEach
    fun setup() {
        getSweetsWithNoEggsUseCase = mockk(relaxed = true)
        mealPrinter = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        sweetsWithNoEggsUi = SweetsWithNoEggsUi(getSweetsWithNoEggsUseCase = getSweetsWithNoEggsUseCase, mealPrinter = mealPrinter, viewer = viewer, reader)
    }

    @Test
    fun `should print a short meal and then print full meal when use case returns meals and user likes the meal`() {
        //Given
        val meals = listOf(
            createMeal(
                name = "Vegan cupcake",
                tags = listOf(TAG_SWEET),
                ingredients = listOf("flour", "sugar", "vegan butter")
            ),
        )
        every { getSweetsWithNoEggsUseCase() } returns Result.success(meals)
        every { reader.readInt() } returns 0
        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 1) { mealPrinter.printShortMeal(any()) }
        verify(exactly = 1) { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should display error message when user enters invalid suggestion feedback`() {
        //Given
        val meals = listOf(
            createMeal(
                name = "Vegan cupcake",
                tags = listOf(TAG_SWEET),
                ingredients = listOf("flour", "sugar", "vegan butter")
            ),
        )
        every { getSweetsWithNoEggsUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(2, 0)
        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 1) { viewer.display(SweetsWithNoEggsUi.INVALID_OPTION_MESSAGE) }
    }

    @Test
    fun `should print multiple short meals and then print full meal when use case returns more than one meal and user likes the meal`() {
        //Given
        val meals = listOf(
            createMeal(
                name = "Vegan cupcake",
                tags = listOf(TAG_SWEET),
                ingredients = listOf("flour", "sugar", "vegan butter")
            ),
            createMeal(
                name = "Ice cream",
                tags = listOf(TAG_SWEET),
                ingredients = listOf("cream", "vanilla", "ice")
            ),
        )
        every { getSweetsWithNoEggsUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(1, 0)
        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 2) { mealPrinter.printShortMeal(any()) }
        verify(exactly = 1) { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should print out of meals message when user doesn't like all the meals`() {
        //Given
        val meals = listOf(
            createMeal(
                name = "Vegan cupcake",
                tags = listOf(TAG_SWEET),
                ingredients = listOf("flour", "sugar", "vegan butter")
            ),
            createMeal(
                name = "Ice cream",
                tags = listOf(TAG_SWEET),
                ingredients = listOf("cream", "vanilla", "ice")
            ),
        )
        every { getSweetsWithNoEggsUseCase() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(1, 1)
        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 1) { viewer.display(SweetsWithNoEggsUi.OUT_OF_MEALS_MESSAGE) }
    }

    @Test
    fun `should display error message when use case returns failure result with NoMealsFoundException`() {
        //Given
        every { getSweetsWithNoEggsUseCase() } returns Result.failure(NoMealsFoundException())

        //When
        sweetsWithNoEggsUi.startSweetsWithNoEggsFlow()

        //Then
        verify(exactly = 4) { viewer.display(any()) }
    }
}