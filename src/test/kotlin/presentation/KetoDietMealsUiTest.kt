import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.model.Meal
import logic.model.Nutrition
import logic.useCase.GetKetoDietMealUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.presentation.KetoDietMealsUi
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import org.seoulsquad.presentation.utils.SuggestionFeedbackOption

class KetoDietMealsUiTest {

    private lateinit var getKetoDietMealUseCase: GetKetoDietMealUseCase
    private lateinit var mealPrinter: MealPrinter
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var ketoDietMealsUi: KetoDietMealsUi

    private val sampleMeal = Meal(
        name = "Zucchini Noodles",
        id = 1,
        preparationTimeInMinutes = 15,
        contributorId = 42,
        submittedAt = null,
        tags = listOf("keto", "low-carb"),
        nutrition = Nutrition(
            calories = 200.0,
            totalFat = 14.0,
            sugar = 2.0,
            sodium = 300.0,
            protein = 10.0,
            saturatedFat = 4.0,
            carbohydrates = 6.0
        ),
        numberOfSteps = 2,
        steps = listOf("Spiralize the zucchini", "Add pesto"),
        description = "Zucchini noodles with fresh pesto.",
        ingredients = listOf("Zucchini", "Pesto", "Olive oil"),
        numberOfIngredients = 3
    )

    private val anotherMeal = Meal(
        name = "Zucchini Noodles",
        id = 1,
        preparationTimeInMinutes = 15,
        contributorId = 42,
        submittedAt = null,
        tags = listOf("keto", "low-carb"),
        nutrition = Nutrition(
            calories = 200.0,
            totalFat = 14.0,
            sugar = 2.0,
            sodium = 300.0,
            protein = 10.0,
            saturatedFat = 4.0,
            carbohydrates = 6.0
        ),
        numberOfSteps = 2,
        steps = listOf("Spiralize the zucchini", "Add pesto"),
        description = "Zucchini noodles with fresh pesto.",
        ingredients = listOf("Zucchini", "Pesto", "Olive oil"),
        numberOfIngredients = 3
    )

    @BeforeEach
    fun setup() {
        getKetoDietMealUseCase = mockk()
        mealPrinter = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk()
        ketoDietMealsUi = KetoDietMealsUi(getKetoDietMealUseCase, mealPrinter, viewer, reader)
    }

    @Test
    fun `should return intro messages when starting the keto diet flow`() {
        //Given
        every { getKetoDietMealUseCase.getKetoDietMeal() } returns Result.success(listOf(sampleMeal))
        every { reader.readInt() } returns SuggestionFeedbackOption.LIKE.ordinal

        //When
        ketoDietMealsUi.startKetoDietFlow()

        //Then
        verify { viewer.display(match { it.contains("Following a Keto diet") }) }
        verify { viewer.display(match { it.contains("like to see full details") }) }
        verify { viewer.display(match { it.contains("Loading Keto meals") }) }
    }

    @Test
    fun `should return error message when getting keto meals fails`() {
        //Given
        every { getKetoDietMealUseCase.getKetoDietMeal() } returns Result.failure(RuntimeException("Network error"))

        //When
        ketoDietMealsUi.startKetoDietFlow()

        //Then
        verify { viewer.display("Error: Network error") }
    }

    @Test
    fun `should return full meal details when user likes the suggestion`() {
        //Given
        every { getKetoDietMealUseCase.getKetoDietMeal() } returns Result.success(listOf(sampleMeal))
        every { reader.readInt() } returns SuggestionFeedbackOption.LIKE.ordinal

        //When
        ketoDietMealsUi.startKetoDietFlow()

        //Then
        verify { mealPrinter.printShortMeal(sampleMeal) }
        verify { mealPrinter.printFullMeal(sampleMeal) }
    }

    @Test
    fun `should return another suggestion when user dislikes the current meal`() {
        //Given
        val meals = listOf(sampleMeal, anotherMeal)
        every { getKetoDietMealUseCase.getKetoDietMeal() } returns Result.success(meals)
        every { reader.readInt() } returnsMany listOf(
            SuggestionFeedbackOption.DISLIKE.ordinal,
            SuggestionFeedbackOption.LIKE.ordinal
        )

        //When
        ketoDietMealsUi.startKetoDietFlow()

        //Then
        verify(exactly = 2) { mealPrinter.printShortMeal(any()) }
        verify { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should return invalid option message when user input is not a valid option`() {
        //Given
        every { getKetoDietMealUseCase.getKetoDietMeal() } returns Result.success(listOf(sampleMeal))
        every { reader.readInt() } returnsMany listOf(-99, SuggestionFeedbackOption.LIKE.ordinal)

        //When
        ketoDietMealsUi.startKetoDietFlow()

        //Then
        verify { viewer.display(KetoDietMealsUi.INVALID_OPTION_MESSAGE) }
        verify { mealPrinter.printFullMeal(sampleMeal) }
    }

    @Test
    fun `should return out of meals message when meal list is empty`() {
        //Given
        every { getKetoDietMealUseCase.getKetoDietMeal() } returns Result.success(emptyList())

        //When
        ketoDietMealsUi.startKetoDietFlow()

        //Then
        verify { viewer.display(KetoDietMealsUi.OUT_OF_MEALS_MESSAGE) }
    }

    @Test
    fun `should return invalid option message when user input is null`() {
        //Given
        every { getKetoDietMealUseCase.getKetoDietMeal() } returns Result.success(listOf(sampleMeal))
        every { reader.readInt() } returnsMany listOf(null, SuggestionFeedbackOption.LIKE.ordinal)

        //When
        ketoDietMealsUi.startKetoDietFlow()

        //Then
        verify { viewer.display(KetoDietMealsUi.INVALID_OPTION_MESSAGE) }
        verify { mealPrinter.printFullMeal(sampleMeal) }
    }

}

