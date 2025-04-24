package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.utils.NoMealsFoundException
import utils.createMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.useCase.GetSeafoodMealsSortedByProteinUseCase
import org.seoulsquad.presentation.SeaFoodMealsSortedByProteinUi
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.TablePrinter
import utils.createNutritionWithProtein

class SeaFoodMealsSortedByProteinUiTest {
    private lateinit var tablePrinter: TablePrinter
    private lateinit var getSeafoodMealsSortedByProteinUseCase: GetSeafoodMealsSortedByProteinUseCase
    private lateinit var viewer: Viewer
    private lateinit var seaFoodMealsSortedByProteinUi: SeaFoodMealsSortedByProteinUi

    private val meals = listOf(
        createMeal(id = 1, name = "Shrimp", tags = listOf("SEAFOOD"), nutrition = createNutritionWithProtein(protein = 20.0))
    )

    @BeforeEach
    fun setup() {
        tablePrinter = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        getSeafoodMealsSortedByProteinUseCase = mockk(relaxed = true)
        seaFoodMealsSortedByProteinUi = SeaFoodMealsSortedByProteinUi(
            getSortedSeafoodMealsUseCase = getSeafoodMealsSortedByProteinUseCase,
            viewer = viewer,
            tablePrinter = tablePrinter,
        )
    }

    @Test
    fun `should print meals table when use case returns success result with list of meals`(){
        //Given
        every { getSeafoodMealsSortedByProteinUseCase() } returns Result.success(meals)

        //When
        seaFoodMealsSortedByProteinUi.startSeafoodMealsSortedByProtein()

        //Then
        verify (exactly = 1) { tablePrinter.printTable(any(), any()) }
    }

    @Test
    fun `should print error message when use case returns failure result`(){
        //Given
        every { getSeafoodMealsSortedByProteinUseCase() } returns Result.failure(NoMealsFoundException())

        //When
        seaFoodMealsSortedByProteinUi.startSeafoodMealsSortedByProtein()

        //Then
        verify (exactly = 1) { viewer.display(NoMealsFoundException().message) }
    }
}