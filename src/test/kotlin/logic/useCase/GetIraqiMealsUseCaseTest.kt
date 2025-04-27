package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.NoMealsFoundException
import mockData.creatIraqiMeals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase

class GetIraqiMealsUseCaseTest{
    private lateinit var mealRepository: MealRepository
    private lateinit var getIraqiMealsUseCase: GetIraqiMealsUseCase
    private val mealsWithIraqInTags = listOf(
        creatIraqiMeals(id = 1, tags = listOf("mexico", "spicy"), description = "taco"),
        creatIraqiMeals(id = 2, tags = listOf("iRaQ", "dinner", "spicy"), description = "traditional plate"),
        creatIraqiMeals(id = 3, tags = listOf("iraq", "vegan"), description = "healthy option"),
        creatIraqiMeals(id = 4, tags = listOf("Iraq", "weeknight", "course"), description = "this is an egyptian dish ")
    )

    private val mealsWithIraqInDescription = listOf(
        creatIraqiMeals(id = 1, tags = listOf("italy", "pasta"), description = "traditional italian pasta"),
        creatIraqiMeals(id = 2, tags = listOf("vegan"), description = "IrAq is known for this"),
        creatIraqiMeals(id = 3, tags = listOf("dinner", "spicy"), description = "iraq special meal"),
        creatIraqiMeals(id = 4, tags = listOf("lunch", "weeknight", "course"), description = "this is an Iraq dish ")
    )


    private val mealsWithIraqInBoth = listOf(
        creatIraqiMeals(id = 1, tags = listOf("mexico"), description = "not related"),
        creatIraqiMeals(id = 2, tags = listOf("iraq"), description = "iraq style"),
        creatIraqiMeals(id = 3, tags = listOf("iRaQ", "weeknight", "course"), description = "this is an IrAq  dish "),
        creatIraqiMeals(id = 4, tags = listOf("Iraq", "lunch"), description = "iraqi meal")
    )

    private val mealsWithEmptyOrNullTagsOrDescription = listOf(
        creatIraqiMeals(id = 1, tags = listOf("iraq"), description = null),
        creatIraqiMeals(id = 2, tags = listOf(), description = "this is an iraqi dish "),
        creatIraqiMeals(id = 3, tags = listOf("Iraq", "weeknight", "course"), description = "")
    )

    private val mealsWithoutIraqKeyword = listOf(
        creatIraqiMeals(id = 1, tags = listOf("Italy", "weeknight", "course"), description = "pizza"),
        creatIraqiMeals(id = 2, tags = listOf("USA", "breakfast"), description = "eggs and bacon")
    )

    @BeforeEach
    fun setup(){
        mealRepository = mockk(relaxed = true)
        getIraqiMealsUseCase = GetIraqiMealsUseCase(mealRepository)
    }

    @Test
    fun `should return iraqi meals when tags only include  Iraq keyword in any format`() {
        // given
        every { mealRepository.getAllMeals() } returns mealsWithIraqInTags
        // when
        val result = getIraqiMealsUseCase()
        // then
        val expectedIraqiMealsIds = setOf(2,3,4)
        val actualReturnedMealsIds = result.getOrNull()?.map { it.id }
        assertThat(expectedIraqiMealsIds).containsAtLeastElementsIn(actualReturnedMealsIds)
    }

    @Test
    fun `should return iraqi meals when description only include Iraq keyword in any format`() {
        // given
        every { mealRepository.getAllMeals() } returns mealsWithIraqInDescription
        // when
        val result = getIraqiMealsUseCase()
        // then
        val expectedIraqiMealsIds = setOf(2, 3, 4)
        val actualReturnedMealsIds = result.getOrNull()?.map { it.id }
        assertThat(expectedIraqiMealsIds).containsAtLeastElementsIn(actualReturnedMealsIds)
    }
    @Test
    fun `should return iraqi meals when both tags and description include Iraq keyword in any case format`() {
        // given
        every { mealRepository.getAllMeals() } returns mealsWithIraqInBoth
        // when
        val result = getIraqiMealsUseCase()
        // then
        val expectedIraqiMealsIds = setOf(2, 3, 4)
        val actualReturnedMealsIds = result.getOrNull()?.map { it.id }
        assertThat(expectedIraqiMealsIds).containsAtLeastElementsIn(actualReturnedMealsIds)
    }

    @Test
    fun `should return iraqi meals when either tags or description include iraqi keyword even if the other is empty or null`() {
        // given
        every { mealRepository.getAllMeals() } returns mealsWithEmptyOrNullTagsOrDescription
        // when
        val result = getIraqiMealsUseCase()
        // then
        val expectedIraqiMealsIds = setOf(1, 2, 3)
        val actualReturnedMealsIds = result.getOrNull()?.map { it.id }
        assertThat(expectedIraqiMealsIds).containsAtLeastElementsIn(actualReturnedMealsIds)
    }
    @Test
    fun `should return failure when no meals include Iraq keyword in tags and description`() {
        // given
        every { mealRepository.getAllMeals() } returns mealsWithoutIraqKeyword
        // when
        val result = getIraqiMealsUseCase()
        // then
        assertThrows<NoMealsFoundException> {
            result.getOrThrow()
        }
    }
    @Test
    fun `should return failure when repository returns an empty list`() {
        // given
        val meals = emptyList<Meal>()
        every { mealRepository.getAllMeals() } returns meals
        // when
        val result = getIraqiMealsUseCase()
        // then
        assertThrows<NoMealsFoundException> {
            result.getOrThrow()
        }
    }
}
