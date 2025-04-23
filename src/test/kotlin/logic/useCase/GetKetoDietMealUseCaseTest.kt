package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.utils.NoMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.seoulsquad.logic.repository.MealRepository
import kotlin.test.Test


class GetKetoDietMealUseCaseTest {
    private lateinit var mealRepo: MealRepository
    private lateinit var getKetoDietMealUseCase: GetKetoDietMealUseCase

    @BeforeEach
    fun setup() {
        mealRepo = mockk()
        getKetoDietMealUseCase = GetKetoDietMealUseCase(mealRepo)
    }

    @Test
    fun `should return keto meals when all conditions are met`() {
        // Given
        val meals = listOf(createMeal(40.0, 25.0, 10.0, 2.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.size).isEqualTo(1)
    }

    @Test
    fun `should return failure when fat percent is too low`() {
        // Given
        val meals = listOf(createMeal(48.0, 15.0, 10.0, 4.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return failure when fat percent is too high`() {
        // Given
        val meals = listOf(createMeal(76.0, 20.0, 10.0, 4.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return failure when protein percent is too low`() {
        // Given
        val meals = listOf(createMeal(50.0, 13.0, 10.0, 4.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return failure when protein percent is too high`() {
        // Given
        val meals = listOf(createMeal(50.0, 26.0, 10.0, 4.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return failure when carb percent is too low`() {
        // Given
        val meals = listOf(createMeal(50.0, 16.0, 4.0, 4.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return failure when carb percent is too high`() {
        // Given
        val meals = listOf(createMeal(50.0, 14.0, 11.0, 4.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return failure when net carbs are too high`() {
        // Given
        val meals = listOf(createMeal(50.0, 14.0, 20.0, 4.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return failure when sugar is too high`() {
        // Given
        val meals = listOf(createMeal(50.0, 15.0, 10.0, 6.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return failure when total calories are zero`() {
        // Given
        val meals = listOf(createMeal(0.0, 0.0, 0.0, 0.0))
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getKetoDietMealUseCase.getKetoDietMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test fun `valid keto meal returns true`() {
        val result = getKetoDietMealUseCase.validateKetoConstraints(72.0, 22.0, 6.0, 15.0, 3.0)
        assertThat(result).isTrue()
    }

    @Test fun `fat percent too low returns false`() {
        val result = getKetoDietMealUseCase.validateKetoConstraints(65.0, 22.0, 6.0, 15.0, 3.0)
        assertThat(result).isFalse()
    }

    @Test fun `fat percent too high returns false`() {
        val result = getKetoDietMealUseCase.validateKetoConstraints(80.0, 22.0, 6.0, 15.0, 3.0)
        assertThat(result).isFalse()
    }

    @Test fun `protein percent too low returns false`() {
        val result = getKetoDietMealUseCase.validateKetoConstraints(72.0, 10.0, 6.0, 15.0, 3.0)
        assertThat(result).isFalse()
    }

    @Test fun `protein percent too high returns false`() {
        val result = getKetoDietMealUseCase.validateKetoConstraints(72.0, 30.0, 6.0, 15.0, 3.0)
        assertThat(result).isFalse()
    }

    @Test fun `carb percent too low returns false`() {
        val result = getKetoDietMealUseCase.validateKetoConstraints(72.0, 22.0, 3.0, 15.0, 3.0)
        assertThat(result).isFalse()
    }

    @Test fun `carb percent too high returns false`() {
        val result = getKetoDietMealUseCase.validateKetoConstraints(72.0, 22.0, 11.0, 15.0, 3.0)
        assertThat(result).isFalse()
    }

    @Test fun `net carbs too high returns false`() {
        val result = getKetoDietMealUseCase.validateKetoConstraints(72.0, 22.0, 6.0, 25.0, 3.0)
        assertThat(result).isFalse()
    }

    @Test fun `sugar too high returns false`() {
        val result = getKetoDietMealUseCase.validateKetoConstraints(72.0, 22.0, 6.0, 15.0, 6.0)
        assertThat(result).isFalse()
    }


}