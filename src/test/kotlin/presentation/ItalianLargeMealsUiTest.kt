package presentation

import GetItalianLargeMealsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.utils.Constants.ITALIAN_NAME
import logic.utils.Constants.LARGE_GROUP_NAME
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.presentation.ItalianLargeMealsUi
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import testData.createItalianLargeMeal

class ItalianLargeMealsUiTest {
    private lateinit var italianLargeMealsUseCase: GetItalianLargeMealsUseCase
    private lateinit var mealPrinter: MealPrinter
    private lateinit var viewer: Viewer
    private lateinit var italianLargeMealsUi: ItalianLargeMealsUi
    private val italianLargeMeal = listOf(
        createItalianLargeMeal(listOf(LARGE_GROUP_NAME, ITALIAN_NAME), "greek italian seafood", 1),
        createItalianLargeMeal(listOf(LARGE_GROUP_NAME, "milk", "berry"), "greek italian seafood", 2),

        )

    @BeforeEach
    fun setUp() {
        italianLargeMealsUseCase = mockk(relaxed = true)
        mealPrinter = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        italianLargeMealsUi = ItalianLargeMealsUi(italianLargeMealsUseCase, mealPrinter, viewer)
    }

    @Test
    fun `should show meals when Italian Large meals available `() {
        //Given
        every { italianLargeMealsUseCase() } returns Result.success(italianLargeMeal)
        //When
        italianLargeMealsUi.startItalianLargeMealsFlow()
        //Then
        verify(exactly = 1) { viewer.display(any()) }
        verify(exactly = 1) { mealPrinter.printSearchResult(any()) }
    }
    @Test
    fun `should return exception when no meals found `() {
        //Given
        every { italianLargeMealsUseCase() } returns Result.failure(NoMealsFoundException())
        //When
        italianLargeMealsUi.startItalianLargeMealsFlow()
        //Then
        verify(exactly = 2) { viewer.display(any()) }
    }
}