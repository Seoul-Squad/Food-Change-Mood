package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.di.MealUiControllers

class ConsoleUiTest {
    private lateinit var mealUiControllers: MealUiControllers
    private lateinit var mainApp: ConsoleUi
    private lateinit var reader: Reader
    private lateinit var viewer: Viewer

    @BeforeEach
    fun setUp() {
        mealUiControllers = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        mainApp = ConsoleUi(mealUiControllers, viewer, reader)
    }

    @Test
    fun `should return healthy meal flow when input is 1`() {
        // Given
        every { reader.readString() } returnsMany listOf("1", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.healthyMealUi.presentHealthyMeal() }
    }

    @Test
    fun `should return search by name flow when input is 2`() {
        // Given
        every { reader.readString() } returnsMany listOf("2", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.searchByNameConsole.searchMealByName() }
    }

    @Test
    fun `should return iraqi meals flow when input is 3`() {
        // Given
        every { reader.readString() } returnsMany listOf("3", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.iraqiMealsUi.startIraqiMealsFlow() }
    }

    @Test
    fun `should return random easy meals when input is 4`() {
        // Given
        every { reader.readString() } returnsMany listOf("4", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.randomEasyMealsUi.printRandomEasyMeals() }
    }

    @Test
    fun `should return guess game flow when input is 5`() {
        // Given
        every { reader.readString() } returnsMany listOf("5", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.guessMealPreparationTimeGameUI.startGuessGame() }
    }

    @Test
    fun `should return sweets with no eggs flow when input is 6`() {
        // Given
        every { reader.readString() } returnsMany listOf("6", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.sweetsWithNoEggsConsole.startSweetsWithNoEggsFlow() }
    }

    @Test
    fun `should return keto diet flow when input is 7`() {
        // Given
        every { reader.readString() } returnsMany listOf("7", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.ketoDietMealsUi.startKetoDietFlow() }
    }

    @Test
    fun `should return search meal using date flow when input is 8`() {
        // Given
        every { reader.readString() } returnsMany listOf("8", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.searchMealUsingDateUi.searchMealUsingDate() }
    }

    @Test
    fun `should return meals by calories and protein flow when input is 9`() {
        // Given
        every { reader.readString() } returnsMany listOf("9", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.mealsByCaloriesAndProteinUi.startGetMealsByCaloriesAndProtein() }
    }

    @Test
    fun `should return explore other countries food flow when input is 10`() {
        // Given
        every { reader.readString() } returnsMany listOf("10", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.exploreOtherCountriesFoodConsole.exploreOtherCountriesFood() }
    }

    @Test
    fun `should return ingredient game flow when input is 11`() {
        // Given
        every { reader.readString() } returnsMany listOf("11", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.ingredientGameUi.startIngredientGameFlow() }
    }

    @Test
    fun `should return show random meals by ingredient flow when input is 12`() {
        // Given
        every { reader.readString() } returnsMany listOf("12", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.showRandomMealsByIngredientUi.startShowRandomMealsByIngredient() }
    }

    @Test
    fun `should return high calories meals flow when input is 13`() {
        // Given
        every { reader.readString() } returnsMany listOf("13", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.mealsWithHighCaloriesUi.getMealsWithHighCalories() }
    }

    @Test
    fun `should return seafood meals sorted by protein flow when input is 14`() {
        // Given
        every { reader.readString() } returnsMany listOf("14", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.seaFoodMealsSortedByProteinConsole.startSeafoodMealsSortedByProtein() }
    }

    @Test
    fun `should return italian large meals flow when input is 15`() {
        // Given
        every { reader.readString() } returnsMany listOf("15", "0")

        // When
        mainApp.start()

        // Then
        verify { mealUiControllers.italianLargeMealsConsole.startItalianLargeMealsFlow() }
    }

    @Test
    fun `should return error message when input is invalid`() {
        // Given
        every { reader.readString() } returnsMany listOf("999", "0")

        // When
        mainApp.start()

        // Then
        verify { viewer.display("❌ Invalid option. Please try again!") }
    }

}
