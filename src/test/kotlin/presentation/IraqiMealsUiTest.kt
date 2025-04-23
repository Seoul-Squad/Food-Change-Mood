package presentation


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.utils.NoMealsFoundException
import mockData.creatIraqiMeals
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import org.seoulsquad.presentation.IraqiMealsUi
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.utils.SharedUi

class IraqiMealsUiTest{
    private lateinit var getIraqiMealsUseCase: GetIraqiMealsUseCase
    private lateinit var viewer: Viewer
    private lateinit var sharedUi: SharedUi
    private lateinit var iraqiMealsUi: IraqiMealsUi

    @BeforeEach
    fun setup(){
        getIraqiMealsUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        sharedUi = mockk(relaxed = true)
        iraqiMealsUi = IraqiMealsUi(getIraqiMealsUseCase, viewer,sharedUi)
    }

    @Test
    fun `when useCase succeeds, should display intro and each meal`() {
          // given
        val iraqiMeals = listOf(
            creatIraqiMeals(tags = listOf("iraq"), description = "iraq style"),
            creatIraqiMeals(tags = listOf("iRaQ", "weeknight", "course"), description = "this is an IrAq  dish "),
        )
        every { getIraqiMealsUseCase() } returns Result.success(iraqiMeals)

          // when
        iraqiMealsUi.startIraqiMealsFlow()

          // then
        verify{
            viewer.display(any())
            viewer.display(any())

            sharedUi.printFullMeal(any())
            viewer.display(any())

            sharedUi.printFullMeal(any())
            viewer.display(any())
        }
     }
    @Test
    fun `when useCase fails, should display intro then error message`() {
        // given
        every { getIraqiMealsUseCase() } returns Result.failure(NoMealsFoundException())

        // when
        iraqiMealsUi.startIraqiMealsFlow()

        // then
        verify {
            viewer.display(any())
            viewer.display(any())
            viewer.display(any())
        }
    }

}