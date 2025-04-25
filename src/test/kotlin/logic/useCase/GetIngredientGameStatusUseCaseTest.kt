package logic.useCase

import com.google.common.truth.Truth.assertThat
import logic.utils.InvalidNumberException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.useCase.GetIngredientGameStatusUseCase

class GetIngredientGameStatusUseCaseTest {
    private lateinit var getIngredientGameStatusUseCase: GetIngredientGameStatusUseCase
    private val mockQuestion =
        IngredientQuestion(
            id = 1,
            mealName = "Mock Meal",
            chooses =
                listOf(
                    false to "Wrong Answer 1",
                    true to "Correct Answer",
                    false to "Wrong Answer 2",
                ),
        )

    @BeforeEach
    fun setUp() {
        getIngredientGameStatusUseCase = GetIngredientGameStatusUseCase()
    }

    @Test
    fun `should return success result with increased score when answer is correct`() {
        // Given
        val correctAnswer = 2

        // When
        val result = getIngredientGameStatusUseCase(correctAnswer, mockQuestion)

        // Then
        assertThat(result.isSuccess).isTrue()
        val status = result.getOrNull()
        assertThat(status).isNotNull()
        assertThat(status?.totalScore).isEqualTo(GetIngredientGameStatusUseCase.Companion.SCORE_PER_ROUND)
        assertThat(status?.isGameOver).isFalse()
    }

    @Test
    fun `should return success result with game over when answer is incorrect`() {
        // Given
        val wrongAnswer = 1

        // When
        val result = getIngredientGameStatusUseCase(wrongAnswer, mockQuestion)

        // Then
        assertThat(result.isSuccess).isTrue()
        val status = result.getOrNull()
        assertThat(status).isNotNull()
        assertThat(status?.totalScore).isEqualTo(0)
        assertThat(status?.isGameOver).isTrue()
    }

    @Test
    fun `should return failure with InvalidNumberException when user answer is null`() {
        // Given
        val userAnswer = null

        // When/Then
        assertThrows<InvalidNumberException> {
            getIngredientGameStatusUseCase(userAnswer, mockQuestion).getOrThrow()
        }
    }

    @Test
    fun `should return failure with InvalidNumberException when user answer is out of bounds`() {
        // Given
        val userAnswer = 10

        // When/Then
        assertThrows<InvalidNumberException> {
            getIngredientGameStatusUseCase(userAnswer, mockQuestion).getOrThrow()
        }
    }

    @Test
    fun `should return failure with InvalidNumberException when user answer is zero`() {
        // Given
        val userAnswer = 0

        // When/Then
        assertThrows<InvalidNumberException> {
            getIngredientGameStatusUseCase(userAnswer, mockQuestion).getOrThrow()
        }
    }

    @Test
    fun `should return failure with InvalidNumberException when user answer is negative`() {
        // Given
        val userAnswer = -5

        // When/Then
        assertThrows<InvalidNumberException> {
            getIngredientGameStatusUseCase(userAnswer, mockQuestion).getOrThrow()
        }
    }

    @Test
    fun `should reset score when game is over and new answer is submitted`() {
        // Given
        val wrongAnswer = 1
        getIngredientGameStatusUseCase(wrongAnswer, mockQuestion)

        // When
        val correctAnswer = 2
        val result = getIngredientGameStatusUseCase(correctAnswer, mockQuestion)

        // Then
        assertThat(result.isSuccess).isTrue()
        val status = result.getOrNull()
        assertThat(status).isNotNull()
        assertThat(status?.totalScore).isEqualTo(GetIngredientGameStatusUseCase.SCORE_PER_ROUND)
        assertThat(status?.isGameOver).isFalse()
    }

    @Test
    fun `should accumulate score when consecutive correct answers are given`() {
        // Given
        val correctAnswer = 2

        // When
        // Submit correct answer multiple times
        getIngredientGameStatusUseCase(correctAnswer, mockQuestion)
        getIngredientGameStatusUseCase(correctAnswer, mockQuestion)
        val result = getIngredientGameStatusUseCase(correctAnswer, mockQuestion)

        // Then
        assertThat(result.isSuccess).isTrue()
        val status = result.getOrNull()
        assertThat(status).isNotNull()
        assertThat(status?.totalScore).isEqualTo(3 * GetIngredientGameStatusUseCase.Companion.SCORE_PER_ROUND)
        assertThat(status?.isGameOver).isFalse()
    }

    @Test
    fun `should return same score when game is over and trying with wrong answer again`() {
        // Given
        val correctAnswer = 1
        val wrongAnswer = 2
        getIngredientGameStatusUseCase(wrongAnswer, mockQuestion)

        getIngredientGameStatusUseCase(correctAnswer, mockQuestion)

        // When
        val result = getIngredientGameStatusUseCase(wrongAnswer, mockQuestion)

        // Then
        assertThat(result.isSuccess).isTrue()
        val status = result.getOrNull()
        assertThat(status).isNotNull()
        assertThat(status?.totalScore).isEqualTo(GetIngredientGameStatusUseCase.Companion.SCORE_PER_ROUND)
        assertThat(status?.isGameOver).isFalse()
    }
}
