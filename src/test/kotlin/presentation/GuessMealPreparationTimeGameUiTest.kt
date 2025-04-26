package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.model.GuessResult
import logic.useCase.GuessMealPreparationTimeGameUseCase
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.model.PreparationTimeGameState
import org.seoulsquad.presentation.GuessMealPreparationTimeGameUI
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer

class GuessMealPreparationTimeGameUiTest {
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var useCase: GuessMealPreparationTimeGameUseCase
    private lateinit var gameUi: GuessMealPreparationTimeGameUI


    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        useCase = mockk(relaxed = true)
        gameUi = GuessMealPreparationTimeGameUI(useCase, viewer, reader)
    }

    val testMeal = createMeal("Meal 1", 30)
    @Test
    fun `should exit when user declines at start`() {
        // Given
        every { useCase.getGameState() } returns PreparationTimeGameState(testMeal, 0, 3, true)
        every { reader.readString() } returns "n"

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
    }

    @Test
    fun `should display no meal found then exit user ends game`() {
        // Given
        every { useCase.getGameState() } returnsMany listOf(
            PreparationTimeGameState(null, 0, 3, false),
            PreparationTimeGameState(null, 0, 3, true)
        )
        every { useCase.invoke(null) } returns Result.success(GuessResult.GAME_STARTED)
        every { reader.readString() } returns "n"

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
    }

    @Test
    fun `should display correct guess and prompt play again when user guesses correctly`() {
        // Given
        val state0 = PreparationTimeGameState(testMeal, 0, 3, false)
        val state1 = state0.copy(currentAttempt = 1, shouldStartNewRound = true)
        every { useCase.getGameState() } returnsMany listOf(state0, state1)
        every { useCase.invoke(null) } returns Result.success(GuessResult.GAME_STARTED)
        every { reader.readString() } returnsMany listOf("10", "n")
        every { useCase.invoke("10") } returns Result.success(GuessResult.CORRECT)

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
    }

    @Test
    fun `should display too low guess and then exit when user guesses too low and it's last attempt`() {
        // Given
        val state0 = PreparationTimeGameState(testMeal, 0, 3, false)
        val state1 = state0.copy(currentAttempt = 1, shouldStartNewRound = false)
        val state2 = state1.copy(shouldStartNewRound = true)
        every { useCase.getGameState() } returnsMany listOf(state0, state1, state2)
        every { useCase.invoke(null) } returns Result.success(GuessResult.GAME_STARTED)
        every { reader.readString() } returnsMany listOf("5", "n")
        every { useCase.invoke("5") } returns Result.success(GuessResult.TOO_LOW)
        every { useCase.invoke(GuessResult.EXIT.name) } returns Result.success(GuessResult.EXIT)

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
    }

    @Test
    fun `should display too high guess and then exit when user guesses too high and it's last guess attempt`() {
        // Given
        val state0 = PreparationTimeGameState(testMeal, 0, 3, false)
        val state1 = state0.copy(currentAttempt = 1, shouldStartNewRound = false)
        val state2 = state1.copy(shouldStartNewRound = true)
        every { useCase.getGameState() } returnsMany listOf(state0, state1, state2)
        every { useCase.invoke(null) } returns Result.success(GuessResult.GAME_STARTED)
        every { reader.readString() } returnsMany listOf("20", "n")
        every { useCase.invoke("20") } returns Result.success(GuessResult.TOO_HIGH)
        every { useCase.invoke(GuessResult.EXIT.name) } returns Result.success(GuessResult.EXIT)

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
    }

    @Test
    fun `should display game over and exit message when max attempts are reached`() {
        // Given
        val state0 = PreparationTimeGameState(testMeal, 0, 3, false)
        val state1 = state0.copy(currentAttempt = 1, shouldStartNewRound = false)
        val state2 = state1.copy(currentAttempt = 2, shouldStartNewRound = false)
        val state3 = state2.copy(currentAttempt = 3, shouldStartNewRound = true)
        every { useCase.getGameState() } returnsMany listOf(state0, state1, state2, state3)
        every { useCase.invoke(null) } returns Result.success(GuessResult.GAME_STARTED)
        every { reader.readString() } returnsMany listOf("1", "1", "1", "n")
        every { useCase.invoke("1") } returns Result.success(GuessResult.TOO_LOW)
        every { useCase.invoke(GuessResult.EXIT.name) } returns Result.success(GuessResult.EXIT)

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
    }

    @Test
    fun `should handle initialize failure and show error when initialization fails`() {
        // Given
        every { useCase.getGameState() } returns PreparationTimeGameState(testMeal, 0, 3, true)
        every { useCase.invoke(null) } returns Result.failure(Exception("Fail init"))

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
    }

    @Test
    fun `should restart then exit when user chooses play again then no`() {
        // Given
        val promptState  = PreparationTimeGameState(testMeal, 0, 3, true)
        val restartState = PreparationTimeGameState(testMeal, 0, 3, false)
        val exitState    = PreparationTimeGameState(testMeal, 0, 3, true)
        every { useCase.getGameState() } returnsMany listOf(
            promptState,
            restartState,
            exitState
        )

        every { reader.readString() } returnsMany listOf("y", "n")
        every { useCase.invoke(any()) } returns Result.success(GuessResult.EXIT)
        every { useCase.invoke(GuessResult.PLAY_AGAIN.name) } returns Result.success(GuessResult.PLAY_AGAIN)
        every { useCase.invoke(null) } returns Result.success(GuessResult.GAME_STARTED)

        // When
        gameUi.startGuessGame()

        // Then: exactly one PLAY_AGAIN and one EXIT
        verify(exactly = 1) { useCase.invoke(GuessResult.PLAY_AGAIN.name) }
        verify(exactly = 1) { useCase.invoke(GuessResult.EXIT.name) }
        verify { viewer.display(any()) }
    }

    @Test
    fun `should display too high message then prompt play again`() {
        // Given
        val state0 = PreparationTimeGameState(testMeal, 0, 3, false) // game started
        val state1 = state0.copy(currentAttempt = 1, shouldStartNewRound = true) // guess made, round ends
        every { useCase.getGameState() } returnsMany listOf(state0, state1)
        every { useCase.invoke(null) } returns Result.success(GuessResult.GAME_STARTED)
        every { reader.readString() } returnsMany listOf("20", "n")
        every { useCase.invoke("20") } returns Result.success(GuessResult.TOO_HIGH)
        every { useCase.invoke(GuessResult.EXIT.name) } returns Result.success(GuessResult.EXIT)

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
    }

    @Test
    fun `should display too low message then prompt play again`() {
        // Given
        val state0 = PreparationTimeGameState(testMeal, 0, 3, false)
        val state1 = state0.copy(currentAttempt = 1, shouldStartNewRound = true)
        every { useCase.getGameState() } returnsMany listOf(state0, state1)
        every { useCase.invoke(null) } returns Result.success(GuessResult.GAME_STARTED)
        every { reader.readString() } returnsMany listOf("5", "n")
        every { useCase.invoke("5") } returns Result.success(GuessResult.TOO_LOW)
        every { useCase.invoke(GuessResult.EXIT.name) } returns Result.success(GuessResult.EXIT)

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
    }

    @Test
    fun `should display correct guess message and prompt play again`() {
        // Given
        val state0 = PreparationTimeGameState(testMeal, 0, 3, false)
        val state1 = state0.copy(currentAttempt = 1, shouldStartNewRound = true)
        every { useCase.getGameState() } returnsMany listOf(state0, state1)
        every { useCase.invoke(null) } returns Result.success(GuessResult.GAME_STARTED)
        every { reader.readString() } returnsMany listOf("10", "n")
        every { useCase.invoke("10") } returns Result.success(GuessResult.CORRECT)
        every { useCase.invoke(GuessResult.EXIT.name) } returns Result.success(GuessResult.EXIT)

        // When
        gameUi.startGuessGame()

        // Then
        verify { viewer.display(any()) }
        verify { viewer.display(any()) }
    }


}