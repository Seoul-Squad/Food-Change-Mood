package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.di.PresentationDependencies


class ConsoleUiTest {
    private lateinit var di: PresentationDependencies
    private lateinit var mainApp: ConsoleUi
    private lateinit var reader: Reader
    private lateinit var viewer: Viewer

    @BeforeEach
    fun setUp() {
        di = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        mainApp = ConsoleUi(di, viewer, reader)
    }

    @Test
    fun `when input is 1 then presentHealthyMeal is called`() {
        every { reader.readString() } returnsMany listOf("1", "0")

        mainApp.start()

        verify { di.healthyMealUi.presentHealthyMeal() }
    }

    @Test
    fun `when input is 2 then searchMealByName is called`() {
        every { reader.readString() } returnsMany listOf("2", "0")

        mainApp.start()

        verify { di.searchByNameConsole.searchMealByName() }
    }

    @Test
    fun `when input is 3 then startIraqiMealsFlow is called`() {
        every { reader.readString() } returnsMany listOf("3", "0")
        mainApp.start()
        verify { di.iraqiMealsUi.startIraqiMealsFlow() }
    }

    @Test
    fun `when input is 4 then printRandomEasyMeals is called`() {
        every { reader.readString() } returnsMany listOf("4", "0")
        mainApp.start()
        verify { di.randomEasyMealsUi.printRandomEasyMeals() }
    }

    @Test
    fun `when input is 5 then startGuessGame is called`() {
        every { reader.readString() } returnsMany listOf("5", "0")
        mainApp.start()
        verify { di.guessMealPreparationTimeGameUI.startGuessGame() }
    }

    @Test
    fun `when input is 6 then startSweetsWithNoEggsFlow is called`() {
        every { reader.readString() } returnsMany listOf("6", "0")
        mainApp.start()
        verify { di.sweetsWithNoEggsConsole.startSweetsWithNoEggsFlow() }
    }

    @Test
    fun `when input is 7 then startKetoDietFlow is called`() {
        every { reader.readString() } returnsMany listOf("7", "0")
        mainApp.start()
        verify { di.ketoDietMealsUi.startKetoDietFlow() }
    }

    @Test
    fun `when input is 8 then searchMealUsingDate is called`() {
        every { reader.readString() } returnsMany listOf("8", "0")
        mainApp.start()
        verify { di.searchMealUsingDateUi.searchMealUsingDate() }
    }

    @Test
    fun `when input is 9 then startGetMealsByCaloriesAndProtein is called`() {
        every { reader.readString() } returnsMany listOf("9", "0")
        mainApp.start()
        verify { di.mealsByCaloriesAndProteinUi.startGetMealsByCaloriesAndProtein() }
    }

    @Test
    fun `when input is 10 then exploreOtherCountriesFood is called`() {
        every { reader.readString() } returnsMany listOf("10", "0")
        mainApp.start()
        verify { di.exploreOtherCountriesFoodConsole.exploreOtherCountriesFood() }
    }

    @Test
    fun `when input is 11 then startIngredientGame is called`() {
        every { reader.readString() } returnsMany listOf("11", "0")
        mainApp.start()
        verify { di.ingredientGameUi.startIngredientGame() }
    }

    @Test
    fun `when input is 12 then startShowRandomPotatoMeals is called`() {
        every { reader.readString() } returnsMany listOf("12", "0")
        mainApp.start()
        verify { di.showRandomPotatoMealsUi.startShowRandomPotatoMeals() }
    }

    @Test
    fun `when input is 13 then getMealsWithHighCalories is called`() {
        every { reader.readString() } returnsMany listOf("13", "0")
        mainApp.start()
        verify { di.mealsWithHighCaloriesUi.getMealsWithHighCalories() }
    }

    @Test
    fun `when input is 14 then startSeafoodMealsSortedByProtein is called`() {
        every { reader.readString() } returnsMany listOf("14", "0")
        mainApp.start()
        verify { di.seaFoodMealsSortedByProteinConsole.startSeafoodMealsSortedByProtein() }
    }

    @Test
    fun `when input is 15 then startItalianLargeMealsFlow is called`() {
        every { reader.readString() } returnsMany listOf("15", "0")
        mainApp.start()
        verify { di.italianLargeMealsConsole.startItalianLargeMealsFlow() }
    }

    @Test
    fun `when input is invalid then error message is displayed`() {
        every { reader.readString() } returnsMany listOf("999", "0")
        mainApp.start()
        verify { viewer.display("❌ Invalid option. Please try again!") }
    }

}