package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.GuessResult
import logic.utils.InvalidNumberException
import logic.utils.NoMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.seoulsquad.logic.repository.MealRepository
import java.util.stream.Stream

class GuessMealPreparationTimeGameUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var guessMealPreparationTimeGameUseCase: GuessMealPreparationTimeGameUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        guessMealPreparationTimeGameUseCase = GuessMealPreparationTimeGameUseCase(mealRepository)
    }

    @Test
    fun `should initialize game and return game started when invoked with null current meal`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(createMeal("Meal 1", 30))
        // When
        val result = guessMealPreparationTimeGameUseCase.invoke(null)
        // Then
        assertThat(result.getOrNull()).isEqualTo(GuessResult.GAME_STARTED)
    }

    @Test
    fun `should return correct guess result when user guesses correctly`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(createMeal("Meal 1", 30))
        guessMealPreparationTimeGameUseCase.invoke(null)
        // When
        val result = guessMealPreparationTimeGameUseCase.invoke("30")
        // Then
        assertThat(result.getOrNull()).isEqualTo(GuessResult.CORRECT)
    }

    @ParameterizedTest
    @MethodSource("provideGuessScenarios")
    fun `should return appropriate guess result`(guess: String, expectedGuessResult: GuessResult) {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(createMeal("Meal 1", 30))
        guessMealPreparationTimeGameUseCase.invoke(null)
        // When
        val result = guessMealPreparationTimeGameUseCase.invoke(guess)
        // Then
        assertThat(result.getOrNull()).isEqualTo(expectedGuessResult)
    }

    @Test
    fun `should return failure when guess is invalid`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(createMeal("Meal 1", 30))
        guessMealPreparationTimeGameUseCase.invoke(null)
        // When
        val result = guessMealPreparationTimeGameUseCase.invoke("invalid")
        // Then
        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidNumberException::class.java)
    }

    @Test
    fun `should return play again when invoked with play again guess`() {
        // When
        val result = guessMealPreparationTimeGameUseCase.invoke(GuessResult.PLAY_AGAIN.name)
        // Then
        assertThat(result.getOrNull()).isEqualTo(GuessResult.PLAY_AGAIN)
    }

    @Test
    fun `should reset game state when play again is invoked`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(createMeal("Meal 1", 30))
        guessMealPreparationTimeGameUseCase.invoke(null)
        // When
        guessMealPreparationTimeGameUseCase.invoke(GuessResult.PLAY_AGAIN.name)
        // Then
        val gameState = guessMealPreparationTimeGameUseCase.getGameState()
        assertThat(gameState.currentMeal).isNull()
        assertThat(gameState.currentAttempt).isEqualTo(0)
        assertThat(gameState.shouldStartNewRound).isFalse()
    }

    @Test
    fun `should return failure when no meals are found`() {
        // Given
        every { mealRepository.getAllMeals() } returns emptyList()
        // When
        val result = guessMealPreparationTimeGameUseCase.invoke(null)
        // Then
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should run full guessing flow with too low and too high attempts until play again prompt`() {
        // Given
        val meal = createMeal("Full Flow Meal", 30)
        every { mealRepository.getAllMeals() } returns listOf(meal)
        // When
        val useCase = GuessMealPreparationTimeGameUseCase(mealRepository)
        val gameStarted = useCase.invoke(null)
        val guess1 = useCase.invoke("10")
        val guess2 = useCase.invoke("50")
        val guess3 = useCase.invoke("30")
        // Then
        assertThat(gameStarted.isSuccess).isTrue()
        assertThat(gameStarted.getOrNull()).isEqualTo(GuessResult.GAME_STARTED)
        assertThat(guess1.getOrNull()).isEqualTo(GuessResult.TOO_LOW)
        assertThat(guess2.getOrNull()).isEqualTo(GuessResult.TOO_HIGH)
        assertThat(guess3.getOrNull()).isEqualTo(GuessResult.CORRECT)
        assertThat(useCase.getGameState().shouldStartNewRound).isTrue()
    }

    @Test
    fun `should handle multiple incorrect guesses leading to game over`() {
        // Given
        val meal = createMeal("Kimchi Stew", 30)
        every { mealRepository.getAllMeals() } returns listOf(meal)
        // When
        val useCase = GuessMealPreparationTimeGameUseCase(mealRepository)
        useCase.invoke(null)
        useCase.invoke("50")
        useCase.invoke("10")
        val finalGuess = useCase.invoke("40")
        // Then
        assertThat(finalGuess.getOrNull()).isEqualTo(GuessResult.TOO_HIGH)
        assertThat(useCase.getGameState().shouldStartNewRound).isTrue()
    }

    @Test
    fun `should play again after game over`() {
        // Given
        val meal = createMeal("Kimchi Stew", 30)
        every { mealRepository.getAllMeals() } returns listOf(meal)
        val useCase = GuessMealPreparationTimeGameUseCase(mealRepository)
        useCase.invoke(null)
        useCase.invoke("50")
        useCase.invoke("10")
        useCase.invoke("40")
        // When
        useCase.invoke(GuessResult.PLAY_AGAIN.name)
        // Then
        val gameState = useCase.getGameState()
        assertThat(gameState.currentMeal).isNull()
        assertThat(gameState.currentAttempt).isEqualTo(0)
        assertThat(gameState.shouldStartNewRound).isFalse()
    }

    @Test
    fun `should return failure when guess is zero`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(createMeal("Meal", 30))
        guessMealPreparationTimeGameUseCase.invoke(null)
        // When
        val result = guessMealPreparationTimeGameUseCase.invoke("0")
        // Then
        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidNumberException::class.java)
    }

    @Test
    fun `should return failure when guess is negative`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(createMeal("Meal", 30))
        guessMealPreparationTimeGameUseCase.invoke(null)
        // When
        val result = guessMealPreparationTimeGameUseCase.invoke("-10")
        // Then
        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidNumberException::class.java)
    }

    @Test
    fun `should return failure if all meals have blank names`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(createMeal("", 30))
        // When
        val result = guessMealPreparationTimeGameUseCase.invoke(null)
        // Then
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should reflect correct game state after some guesses`() {
        // Given
        val meal = createMeal("Meal", 30)
        every { mealRepository.getAllMeals() } returns listOf(meal)
        // When
        guessMealPreparationTimeGameUseCase.invoke(null)
        guessMealPreparationTimeGameUseCase.invoke("10")
        guessMealPreparationTimeGameUseCase.invoke("20")
        // Then
        val state = guessMealPreparationTimeGameUseCase.getGameState()
        assertThat(state.currentMeal).isNotNull()
        assertThat(state.currentAttempt).isEqualTo(2)
        assertThat(state.shouldStartNewRound).isFalse()
        assertThat(state.maxAttempts).isEqualTo(3)
    }

    companion object {
        @JvmStatic
        fun provideGuessScenarios(): Stream<Arguments> = Stream.of(
            Arguments.of("20", GuessResult.TOO_LOW),
            Arguments.of("40", GuessResult.TOO_HIGH),
            Arguments.of("30", GuessResult.CORRECT)
        )
    }
}
