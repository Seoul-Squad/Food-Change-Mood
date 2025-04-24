package presentation

import io.mockk.*
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import logic.model.Meal
import logic.model.Nutrition
import logic.useCase.GetKetoDietMealUseCase
import org.junit.jupiter.api.BeforeEach
import org.seoulsquad.presentation.KetoDietMealsUi
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption
import kotlin.test.Test
import kotlin.Result


class KetoDietMealsUiTest {

    private lateinit var useCase: GetKetoDietMealUseCase
    private lateinit var printer: MealPrinter
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var ui: KetoDietMealsUi

    private val meal = Meal(
        name = "Keto Chicken",
        id = 1,
        preparationTimeInMinutes = 30,
        contributorId = 10,
        submittedAt = null,
        tags = listOf("keto"),
        nutrition = Nutrition(100.0, 10.0, 1.0, 5.0, 20.0, 2.0, 3.0),
        numberOfSteps = 2,
        steps = listOf("Step 1", "Step 2"),
        description = "Delicious keto meal",
        ingredients = listOf("Chicken", "Olive oil"),
        numberOfIngredients = 2
    )

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        printer = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk()
        ui = KetoDietMealsUi(useCase, printer, viewer, reader)
    }

    @Test
    fun `should show meal and print full details when liked`() {
        every { useCase.getKetoDietMeal() } returns Result.success(listOf(meal))
        every { reader.readInt() } returns SuggestionFeedbackOption.LIKE.ordinal

        ui.startKetoDietFlow()

        verify { printer.printFullMeal(meal) }
    }

    @Test
    fun `should suggest another meal when disliked`() {
        val meal2 = meal.copy(id = 2, name = "Keto Beef")
        every { useCase.getKetoDietMeal() } returns Result.success(listOf(meal, meal2))
        every { reader.readInt() } returnsMany listOf(SuggestionFeedbackOption.DISLIKE.ordinal, SuggestionFeedbackOption.LIKE.ordinal)

        ui.startKetoDietFlow()

        verify { printer.printFullMeal(any()) }
    }

    @Test
    fun `should display error when use case fails`() {
        every { useCase.getKetoDietMeal() } returns Result.failure(RuntimeException("Network error"))

        ui.startKetoDietFlow()

        verify { viewer.display("Error: Network error") }
    }

    @Test
    fun `should display message when meal list is empty`() {
        every { useCase.getKetoDietMeal() } returns Result.success(emptyList())

        ui.startKetoDietFlow()

        verify { viewer.display("We are out of meals for now!") }
    }

    @Test
    fun `should display invalid message when user input is invalid`() {
        every { useCase.getKetoDietMeal() } returns Result.success(listOf(meal))
        every { reader.readInt() } returnsMany listOf(99, SuggestionFeedbackOption.LIKE.ordinal)

        ui.startKetoDietFlow()

        verify { viewer.display("Invalid option") }
    }

//    @Test
//    fun `when keto meals list is returned and user likes it then full meal is printed`() {
//        every { useCase.getKetoDietMeal() } returns Result.success(listOf(meal))
//        every { reader.readInt() } returns 0 // LIKE
//
//        ui.startKetoDietFlow()
//
//        verify {printer.printFullMeal(meal) }
//    }
//
//    @Test
//    fun `when user dislikes first meal then likes the second`() {
//        val secondMeal = meal.copy(name = "Keto Soup", id = 2)
//        every { useCase.getKetoDietMeal() } returns Result.success(listOf(meal, secondMeal))
//        every { reader.readInt() } returnsMany listOf(1, 0) // DISLIKE then LIKE
//
//        ui.startKetoDietFlow()
//
//        verify { printer.printFullMeal(any()) }
//    }
//
//    @Test
//    fun `when user inputs invalid option then chooses like`() {
//        every { useCase.getKetoDietMeal() } returns Result.success(listOf(meal))
//        every { reader.readInt() } returnsMany listOf(99, 0) // Invalid then LIKE
//
//        ui.startKetoDietFlow()
//
//        verify { viewer.display("Invalid option") }
//        verify { printer.printFullMeal(meal) }
//    }
//
//    @Test
//    fun `when keto meals list is empty then viewer displays no meals message`() {
//        every { useCase.getKetoDietMeal() } returns Result.success(emptyList())
//
//        ui.startKetoDietFlow()
//
//        verify { viewer.display("We are out of meals for now!") }
//    }
//
//    @Test
//    fun `when keto use case fails then viewer displays error`() {
//        every { useCase.getKetoDietMeal() } returns Result.failure(RuntimeException("Failed to fetch"))
//
//        ui.startKetoDietFlow()
//
//        verify { viewer.display("Error: Failed to fetch") }
//    }



    @Test
    fun `when user selects LIKE, it should print full meal`() {
        every { useCase.getKetoDietMeal() } returns Result.success(listOf(meal))
        every { reader.readInt() } returns SuggestionFeedbackOption.LIKE.ordinal

        ui.startKetoDietFlow()

        verifySequence {
            printer.printShortMeal(meal)
            printer.printLikeAndDislikeOptions()
            printer.printFullMeal(meal)
        }
    }



    @Test
    fun `when user selects DISLIKE, it should suggest another meal`() {
        val secondMeal = meal.copy(id = 2, name = "Keto Chicken")
        every { useCase.getKetoDietMeal() } returns Result.success(listOf(meal, secondMeal))
        every { reader.readInt() } returnsMany listOf(SuggestionFeedbackOption.DISLIKE.ordinal, SuggestionFeedbackOption.LIKE.ordinal)

        ui.startKetoDietFlow()

        verify { printer.printFullMeal(any()) }
        verify(atLeast = 1) { printer.printShortMeal(any()) }
    }






}