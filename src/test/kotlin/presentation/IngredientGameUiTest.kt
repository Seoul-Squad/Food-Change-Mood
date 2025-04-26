package presentation

import io.mockk.*
import logic.utils.InvalidNumberException
import logic.utils.NotEnoughMealsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.model.IngredientGameStatus
import org.seoulsquad.logic.model.IngredientQuestion
import org.seoulsquad.logic.useCase.GetIngredientGameQuestionsUseCase
import org.seoulsquad.logic.useCase.GetIngredientGameStatusUseCase
import org.seoulsquad.presentation.IngredientGameUi
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer

class IngredientGameUiTest {
    private lateinit var ingredientGameUi: IngredientGameUi
    private lateinit var getIngredientGameStatusUseCase: GetIngredientGameStatusUseCase
    private lateinit var getIngredientGameQuestionsUseCase: GetIngredientGameQuestionsUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader

    @BeforeEach
    fun setUp() {
        getIngredientGameQuestionsUseCase = mockk(relaxed = true)
        getIngredientGameStatusUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        ingredientGameUi =
            IngredientGameUi(getIngredientGameStatusUseCase, getIngredientGameQuestionsUseCase, viewer, reader)
    }

    @Test
    fun `should handle successful game flow and exit when user doesn't want to play again`() {
        // Given
        val questions =
            createSampleIngredientQuestions(1)

        val gameStatus =
            IngredientGameStatus(
                isGameOver = false,
                totalScore = 10,
            )

        every { getIngredientGameQuestionsUseCase() } returns Result.success(questions)
        every { getIngredientGameStatusUseCase(any(), any()) } returns Result.success(gameStatus)
        every { viewer.display(any()) } just runs
        every { reader.readInt() } returns 1
        every { reader.readString() } returns "n"

        // When
        ingredientGameUi.startIngredientGameFlow()

        // Then
        verifySequence {
            getIngredientGameQuestionsUseCase()
            viewer.display(any())
            viewer.display(any())
            viewer.display(any())
            viewer.display(any())
            reader.readInt()
            getIngredientGameStatusUseCase(1, questions[0])
            viewer.display(any())
            reader.readString()
        }
    }

    @Test
    fun `should play multiple rounds when user wants to continue`() {
        // Given
        val questions =
            createSampleIngredientQuestions(15)

        val gameStatus =
            IngredientGameStatus(
                isGameOver = false,
                totalScore = 10,
            )

        every { getIngredientGameQuestionsUseCase() } returns Result.success(questions)
        every { getIngredientGameStatusUseCase(any(), any()) } returns Result.success(gameStatus)
        every { viewer.display(any()) } just runs
        every { reader.readInt() } returns 1

        every { reader.readString() } returnsMany listOf("y", "n")

        // When
        ingredientGameUi.startIngredientGameFlow()

        // Then
        verify(exactly = 2) { getIngredientGameQuestionsUseCase() }
        verify(exactly = 2) { reader.readString() }
    }

    @Test
    fun `should handle game over correctly`() {
        // Given
        val questions =
            createSampleIngredientQuestions(15)

        val gameOverStatus =
            IngredientGameStatus(
                isGameOver = true,
                totalScore = 10,
            )

        every { getIngredientGameQuestionsUseCase() } returns Result.success(questions)
        every { getIngredientGameStatusUseCase(any(), any()) } returns Result.success(gameOverStatus)
        every { viewer.display(any()) } just runs
        every { reader.readInt() } returns 1
        every { reader.readString() } returns "n"

        // When
        ingredientGameUi.startIngredientGameFlow()

        // Then
        verifySequence {
            getIngredientGameQuestionsUseCase()
            viewer.display(any())
            viewer.display(any())
            viewer.display(any())
            viewer.display(any())
            reader.readInt()
            getIngredientGameStatusUseCase(1, questions[0])
            viewer.display(any())
            viewer.display(any())
            viewer.display(any())
            reader.readString()
        }
    }

    @Test
    fun `should handle InvalidNumberException correctly`() {
        // Given
        val questions =
            createSampleIngredientQuestions(1)

        val exception = InvalidNumberException()

        every { getIngredientGameQuestionsUseCase() } returns Result.success(questions)
        every { getIngredientGameStatusUseCase(any(), any()) } returns Result.failure(exception)
        every { viewer.display(any()) } just runs
        every { reader.readInt() } returns 5 // Invalid number
        every { reader.readString() } returns "n"

        // When
        ingredientGameUi.startIngredientGameFlow()

        // Then
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
    }

    @Test
    fun `should handle NotEnoughMealsFoundException correctly`() {
        // Given
        val exception = NotEnoughMealsFoundException()

        every { getIngredientGameQuestionsUseCase() } returns Result.failure(exception)
        every { viewer.display(any()) } just runs

        // When
        ingredientGameUi.startIngredientGameFlow()

        // Then
        verify { viewer.display(any()) }
        verify(exactly = 0) { reader.readInt() }
        verify(exactly = 0) { getIngredientGameStatusUseCase(any(), any()) }
    }

    @Test
    fun `startIngredientGameFlow should handle unexpected exceptions correctly`() {
        // Given
        val exception = RuntimeException()

        every { getIngredientGameQuestionsUseCase() } returns Result.failure(exception)
        every { viewer.display(any()) } just runs

        // When
        ingredientGameUi.startIngredientGameFlow()

        // Then
        verify { viewer.display(any()) }
    }

    @Test
    fun `askToPlayAgain should handle invalid inputs correctly`() {
        // Given
        val questions =
            createSampleIngredientQuestions(1)

        val gameStatus =
            IngredientGameStatus(
                isGameOver = false,
                totalScore = 10,
            )

        every { getIngredientGameQuestionsUseCase() } returns Result.success(questions)
        every { getIngredientGameStatusUseCase(any(), any()) } returns Result.success(gameStatus)
        every { viewer.display(any()) } just runs
        every { reader.readInt() } returns 1

        every { reader.readString() } returnsMany listOf("invalid", "n")

        // When
        ingredientGameUi.startIngredientGameFlow()

        // Then
        verifySequence {
            viewer.display(any())
            viewer.display(any())
            viewer.display(any())
            viewer.display(any())
            reader.readInt()
            viewer.display(any())
            reader.readString()
            viewer.display(any())
            reader.readString()
        }
    }

    private fun createSampleIngredientQuestions(count: Int): List<IngredientQuestion> =
        (1..count).map { i ->
            IngredientQuestion(
                id = i,
                mealName = "Meal $i",
                chooses =
                    listOf(
                        true to "Ingredient ${i}A",
                        false to "Ingredient ${i}B",
                        false to "Ingredient ${i}C",
                    ),
            )
        }
}
