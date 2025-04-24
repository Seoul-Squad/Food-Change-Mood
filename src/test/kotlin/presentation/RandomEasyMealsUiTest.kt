package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.useCase.GetRandomEasyMealsUseCase
import logic.utils.NoMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.seoulsquad.presentation.RandomEasyMealsUi
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.MealPrinter
import kotlin.test.Test

class RandomEasyMealsUiTest {
  private lateinit var getRandomEasyMealsUseCase: GetRandomEasyMealsUseCase
  private lateinit var viewer: Viewer
  private lateinit var randomEasyMealsUi: RandomEasyMealsUi
  private lateinit var mealPrinter: MealPrinter

  @BeforeEach
  fun setup() {
      getRandomEasyMealsUseCase = mockk()
      viewer = mockk(relaxed = true)
       mealPrinter = mockk(relaxed = true)
      randomEasyMealsUi = RandomEasyMealsUi(getRandomEasyMealsUseCase, mealPrinter, viewer)
  }

    @Test
    fun `should print random easy meals when use case return success result`() {
        //Given
        every { getRandomEasyMealsUseCase() } returns Result.success(meals)

        //When
        randomEasyMealsUi.printRandomEasyMeals()

        //Then
        verify { mealPrinter.printFullMeal(any()) }
    }

    @Test
    fun `should print exception message when use case return failure result with exception`() {
        //Given
        every { getRandomEasyMealsUseCase() } returns Result.failure(NoMealsFoundException())

        //When
        randomEasyMealsUi.printRandomEasyMeals()

        //Then
        verify { viewer.display(NoMealsFoundException().message.toString()) }
     }


    companion object {
        private val meals = listOf (
            createMeal(1,25, 2, 5),
            createMeal(2,15, 5, 3),
            createMeal(3,18, 3, 4),
            createMeal(4,28, 4, 5),
            createMeal(5,30, 5, 6),
            createMeal(6,25, 2, 5),
            createMeal(7,15, 5, 3),
            createMeal(8,18, 3, 4),
            createMeal(9,28, 4, 5),
            createMeal(10,30, 5, 6),
            createMeal(11,30, 5, 6),
            createMeal(12,30, 5, 6),
        )
    }

 }