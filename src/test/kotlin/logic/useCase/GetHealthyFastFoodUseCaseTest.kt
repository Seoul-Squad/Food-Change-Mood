package logic.useCase


import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.utils.NoMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.GetHealthyFastFoodUseCase

class GetHealthyFastFoodUseCaseTest {

 private lateinit var mealRepository: MealRepository
 private lateinit var useCase: GetHealthyFastFoodUseCase

 @BeforeEach
 fun setUp() {
  mealRepository = mockk(relaxed = true)
  useCase = GetHealthyFastFoodUseCase(mealRepository)
 }

 @Test
 fun `should return the healthy fast meal when one exists`() {
  // Given
  val healthyMeal = createMeal(
   name = "Salad",
   totalFat = 10.0,
   satFat = 5.0,
   carbs = 20.0,
   prepTime = 10
  )
  val dummy = createMeal(
   name = "Dummy",
   totalFat = 100.0,
   satFat = 100.0,
   carbs = 100.0,
   prepTime = 5
  )
  every { mealRepository.getAllMeals() } returns listOf(dummy, healthyMeal)

  // When
  val result = useCase.getFastHealthyMeals()

  // Then
  assertThat(result.isSuccess).isTrue()
  assertThat(result.getOrNull()).containsExactly(healthyMeal)
 }


 @Test
 fun `should throw NoMealsFoundException when meal preparation time exceeds limit`() {
  // Given
  val slowMeal = createMeal(
   name = "SlowStew",
   totalFat = 10.0,
   satFat = 5.0,
   carbs = 20.0,
   prepTime = 20
  )
  val dummy = createMeal(
   name = "Dummy",
   totalFat = 100.0,
   satFat = 100.0,
   carbs = 100.0,
   prepTime = 5
  )
  every { mealRepository.getAllMeals() } returns listOf(dummy, slowMeal)

  // When
  val result = useCase.getFastHealthyMeals()

  // Then
  assertThrows<NoMealsFoundException> {
   result.getOrThrow()
  }
 }

 @Test
 fun `should throw NoMealsFoundException when meal total fat exceeds threshold`() {
  // Given
  val fattyMeal = createMeal(
   name = "Burger",
   totalFat = 50.0,
   satFat = 5.0,
   carbs = 20.0,
   prepTime = 10
  )
  val dummy = createMeal(
   name = "Dummy",
   totalFat = 10.0,
   satFat = 100.0,
   carbs = 100.0,
   prepTime = 5
  )
  every { mealRepository.getAllMeals() } returns listOf(dummy, fattyMeal)

  // When
  val result = useCase.getFastHealthyMeals()

  // Then
  assertThrows<NoMealsFoundException> {
   result.getOrThrow()
  }
 }

 @Test
 fun `should throw NoMealsFoundException when meal saturated fat exceeds threshold`() {
  // Given
  val satFatMeal = createMeal(
   name = "CreamySoup",
   totalFat = 10.0,
   satFat = 50.0,
   carbs = 20.0,
   prepTime = 10
  )
  val dummy = createMeal(
   name = "Dummy",
   totalFat = 100.0,
   satFat = 10.0,
   carbs = 100.0,
   prepTime = 5
  )
  every { mealRepository.getAllMeals() } returns listOf(dummy, satFatMeal)

  // When
  val result = useCase.getFastHealthyMeals()

  // Then
  assertThrows<NoMealsFoundException> {
   result.getOrThrow()
  }
 }

 @Test
 fun `should throw NoMealsFoundException when meal carbohydrates exceeds threshold`() {
  // Given
  val carbMeal = createMeal(
   name = "Pasta",
   totalFat = 10.0,
   satFat = 5.0,
   carbs = 50.0,
   prepTime = 10
  )
  val dummy = createMeal(
   name = "Dummy",
   totalFat = 100.0,
   satFat = 100.0,
   carbs = 10.0,
   prepTime = 5
  )
  every { mealRepository.getAllMeals() } returns listOf(dummy, carbMeal)

  // When
  val result = useCase.getFastHealthyMeals()

  // Then
  assertThrows<NoMealsFoundException> {
   result.getOrThrow()
  }
 }

}