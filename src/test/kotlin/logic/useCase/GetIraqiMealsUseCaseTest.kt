package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.NoMealsFoundException
import mockData.creatIraqiMeals
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import kotlin.test.assertEquals

class GetIraqiMealsUseCaseTest{
    private lateinit var mealRepository: MealRepository
    private lateinit var getIraqiMealsUseCase: GetIraqiMealsUseCase

    @BeforeEach
    fun setup(){
        mealRepository = mockk(relaxed = true)
        getIraqiMealsUseCase = GetIraqiMealsUseCase(mealRepository)
    }

    @Test
    fun `should return iraqi meals when tags only contains Iraq keyword in any format`() {
        // given
        val iraqiMeals = listOf(
            creatIraqiMeals(tags = listOf("mexico", "spicy"), description = "taco"),
            creatIraqiMeals(tags = listOf("iRaQ", "dinner", "spicy"), description = "traditional plate"),
            creatIraqiMeals(tags = listOf("iraq", "vegan"), description = "healthy option"),
            creatIraqiMeals(tags = listOf("Iraq", "weeknight", "course"), description = "this is an egyptian dish "),
        )
        every { mealRepository.getAllMeals() } returns iraqiMeals
        // when
        val result = getIraqiMealsUseCase()
        // then
        assertThat(result.isSuccess).isTrue()
        assertEquals(3, result.getOrNull()?.size)
    }

    @Test
    fun `should return iraqi meals when description only contains Iraq keyword in any format`() {
        // given
        val iraqiMeals = listOf(
            creatIraqiMeals(tags = listOf("italy", "pasta"), description = "traditional italian pasta"),
            creatIraqiMeals(tags = listOf("vegan"), description = "IrAq is known for this"),
            creatIraqiMeals(tags = listOf("dinner", "spicy"), description = "iraq special meal"),
            creatIraqiMeals(tags = listOf("lunch", "weeknight", "course"), description = "this is an Iraq dish "),
        )
        every { mealRepository.getAllMeals() } returns iraqiMeals
        // when
        val result = getIraqiMealsUseCase()
        // then
        assertThat(result.isSuccess).isTrue()
        assertEquals(3, result.getOrNull()?.size)
    }
    @Test
    fun `should return iraqi meals when both tags and description contain Iraq keyword in any case format`() {
        // given
        val iraqiMeals = listOf(
            creatIraqiMeals(tags = listOf("mexico"), description = "not related"),
            creatIraqiMeals(tags = listOf("iraq"), description = "iraq style"),
            creatIraqiMeals(tags = listOf("iRaQ", "weeknight", "course"), description = "this is an IrAq  dish "),
            creatIraqiMeals(tags = listOf("Iraq", "lunch"), description = "iraqi meal"),
        )
        every { mealRepository.getAllMeals() } returns iraqiMeals
        // when
        val result = getIraqiMealsUseCase()
        // then
        assertThat(result.isSuccess).isTrue()
        assertEquals(3, result.getOrNull()?.size)
    }

    @Test
    fun `should return iraqi meals when tags or description are empty or null`() {
        // given
        val iraqiMeals = listOf(
            creatIraqiMeals(tags = listOf("iraq"), description = null),
            creatIraqiMeals(tags = listOf(), description = "this is an iraqi dish "),
            creatIraqiMeals(tags = listOf("Iraq", "weeknight", "course"), description =""),
        )
        every { mealRepository.getAllMeals() } returns iraqiMeals
        // when
        val result = getIraqiMealsUseCase()
        // then
        assertThat(result.isSuccess).isTrue()
        assertEquals(3, result.getOrNull()?.size)
    }
    @Test
    fun `should return failure when no meals contain Iraq keyword in tags and description`() {
        // given
        val iraqiMeals = listOf(
            creatIraqiMeals(tags = listOf("Italy", "weeknight", "course"), description = "pizza"),
            creatIraqiMeals(tags = listOf("USA", "breakfast"), description = "eggs and bacon"),
            creatIraqiMeals(tags = listOf("Mexico", "weeknight", "course"), description = "this is an egyptian dish "),
        )
        every { mealRepository.getAllMeals() } returns iraqiMeals
        // when
        val result = getIraqiMealsUseCase()
        // then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }
    @Test
    fun `should return failure when repository returns an empty list`() {
        // given
        val iraqiMeals = emptyList<Meal>()
        every { mealRepository.getAllMeals() } returns iraqiMeals
        // when
        val result = getIraqiMealsUseCase()
        // then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

}
