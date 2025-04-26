package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.utils.Constants.EXIT
import logic.utils.InvalidDateException
import mockData.createMealForSearchDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.model.MealDate
import org.seoulsquad.logic.useCase.GetMealUsingIDUseCase
import org.seoulsquad.logic.useCase.SearchFoodsUsingDateUseCase
import org.seoulsquad.presentation.SearchMealUsingDateUi
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter

class SearchMealUsingDateUiTest {

    private lateinit var searchMealUsingDateUi: SearchMealUsingDateUi
    private lateinit var searchFoodsUsingDateUseCase: SearchFoodsUsingDateUseCase
    private lateinit var getMealUsingIDUseCase: GetMealUsingIDUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var mealPrinter: MealPrinter
    private val mealsDate: List<MealDate> = listOf(
        MealDate(1, "Grilled Chicken Caesar Salad", LocalDate(monthNumber = 9, dayOfMonth = 6, year = 2024)),
        MealDate(2, "Spaghetti Bolognese", LocalDate(monthNumber = 4, dayOfMonth = 2, year = 2024)),
        MealDate(
            3,
            "Teriyaki Salmon with Steamed Rice",
            LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024)
        ),///
        MealDate(4, "Vegetable Pad Thai", LocalDate(monthNumber = 6, dayOfMonth = 4, year = 2024)),
        MealDate(5, "Beef Tacos with Pico de Gallo", LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024)),///
        MealDate(6, "Margherita Pizza", LocalDate(monthNumber = 7, dayOfMonth = 20, year = 2024))
    )
    private val meal: Meal = createMealForSearchDate(
        3,
        "Teriyaki Salmon with Steamed Rice",
        LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024)
    )

    @BeforeEach
    fun setup() {
        searchFoodsUsingDateUseCase = mockk(relaxed = true)
        getMealUsingIDUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        mealPrinter = mockk(relaxed = true)
        searchMealUsingDateUi =
            SearchMealUsingDateUi(getMealUsingIDUseCase, searchFoodsUsingDateUseCase, viewer, reader, mealPrinter)
    }

    @Test
    fun `should show meals according date and meal when enter valid date and enter valid , existing id`() {

        every { reader.readString() } returnsMany listOf("04-04-2024", EXIT)
        every { reader.readInt() } returnsMany listOf(3, 0)
        every { searchFoodsUsingDateUseCase(any()) } returns Result.success(mealsDate)
        every { getMealUsingIDUseCase(any(), any()) } returns Result.success(meal)


        searchMealUsingDateUi.searchMealUsingDate()

        verify(exactly = 14) { viewer.display(any()) }
        verify(exactly = 1) { mealPrinter.printFullMeal(any()) }

    }

    @Test
    fun `should show meals according date and not show meal when enter valid date and enter invalid id`() {

        every { reader.readString() } returnsMany listOf("04-04-2024", EXIT)
        every { reader.readInt() } returnsMany listOf(-1, 0)
        every { searchFoodsUsingDateUseCase(any()) } returns Result.success(mealsDate)
        every { getMealUsingIDUseCase(any(), any()) } returns Result.failure(InvalidDateException())


        searchMealUsingDateUi.searchMealUsingDate()

        verify(exactly = 15) { viewer.display(any()) }
        verify(exactly = 0) { mealPrinter.printFullMeal(any()) }

    }

    @Test
    fun `should not show meals when enter invalid day at date`() {
        every { reader.readString() } returnsMany listOf("04-lol-2024", EXIT)
        every { searchFoodsUsingDateUseCase(any()) } returns Result.failure(InvalidDateException())

        searchMealUsingDateUi.searchMealUsingDate()

        verify(exactly = 5) { viewer.display(any()) }
        verify(exactly = 0) { mealPrinter.printFullMeal(any()) }
        verify(exactly = 0) { getMealUsingIDUseCase.invoke(any(), any()) }

    }

    @Test
    fun `should not show meals when enter invalid month at date`() {
        every { reader.readString() } returnsMany listOf("lol-04-2024", EXIT)
        every { searchFoodsUsingDateUseCase(any()) } returns Result.failure(InvalidDateException())

        searchMealUsingDateUi.searchMealUsingDate()

        verify(exactly = 5) { viewer.display(any()) }
        verify(exactly = 0) { mealPrinter.printFullMeal(any()) }
        verify(exactly = 0) { getMealUsingIDUseCase.invoke(any(), any()) }

    }

    @Test
    fun `should not show meals when enter invalid year at date`() {
        every { reader.readString() } returnsMany listOf("02-04-lol", EXIT)
        every { searchFoodsUsingDateUseCase(any()) } returns Result.failure(InvalidDateException())

        searchMealUsingDateUi.searchMealUsingDate()

        verify(exactly = 5) { viewer.display(any()) }
        verify(exactly = 0) { mealPrinter.printFullMeal(any()) }
        verify(exactly = 0) { getMealUsingIDUseCase.invoke(any(), any()) }

    }

    @Test
    fun `should not show meals when enter very old year`() {
        every { reader.readString() } returnsMany listOf("02-04-189", EXIT)
        every { searchFoodsUsingDateUseCase(any()) } returns Result.failure(InvalidDateException())

        searchMealUsingDateUi.searchMealUsingDate()

        verify(exactly = 5) { viewer.display(any()) }
        verify(exactly = 0) { mealPrinter.printFullMeal(any()) }
        verify(exactly = 0) { getMealUsingIDUseCase.invoke(any(), any()) }

    }

    @Test
    fun `should not show meals when enter future year`() {
        every { reader.readString() } returnsMany listOf("02-04-2098", EXIT)
        every { searchFoodsUsingDateUseCase(any()) } returns Result.failure(InvalidDateException())

        searchMealUsingDateUi.searchMealUsingDate()

        verify(exactly = 5) { viewer.display(any()) }
        verify(exactly = 0) { mealPrinter.printFullMeal(any()) }
        verify(exactly = 0) { getMealUsingIDUseCase.invoke(any(), any()) }

    }


    @Test
    fun `should not show meals when enter invalid date`() {
        every { reader.readString() } returnsMany listOf("02-04-20-5", EXIT)
        every { searchFoodsUsingDateUseCase(any()) } returns Result.failure(InvalidDateException())

        searchMealUsingDateUi.searchMealUsingDate()

        verify(exactly = 5) { viewer.display(any()) }
        verify(exactly = 0) { mealPrinter.printFullMeal(any()) }
        verify(exactly = 0) { getMealUsingIDUseCase.invoke(any(), any()) }

    }


}