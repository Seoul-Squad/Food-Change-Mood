package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.InvalidNumberException
import logic.utils.NoIngredientFoundException
import logic.utils.NotEnoughMealsFoundException
import mockData.createMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.GetIngredientGameQuestionsUseCase

class GetIngredientGameQuestionsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var getIngredientGameQuestionsUseCase: GetIngredientGameQuestionsUseCase

    @BeforeEach
    fun setUp() {
        mealRepository = mockk()
        getIngredientGameQuestionsUseCase = GetIngredientGameQuestionsUseCase(mealRepository)
    }

    @Test
    fun `should return success result with correct number of questions`() {
        // Given
        val meals = List(10) { createMeal(id = it, ingredients = listOf("Ingredient $it")) }
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getIngredientGameQuestionsUseCase(5)

        // Then
        assertThat(result.isSuccess).isTrue()
        val questions = result.getOrNull()
        assertThat(questions).isNotNull()
        assertThat(questions?.size).isEqualTo(5)
    }

    @Test
    fun `should return questions with correct number of options`() {
        // Given
        val meals = List(20) { createMeal(id = it, ingredients = listOf("Ingredient $it")) }
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getIngredientGameQuestionsUseCase(optionsLimit = 4)

        // Then
        assertThat(result.isSuccess).isTrue()
        val questions = result.getOrNull()
        assertThat(questions).isNotNull()
        questions?.forEach { question ->
            assertThat(question.chooses.size).isEqualTo(4)
        }
    }

    @Test
    fun `should return failure with NotEnoughMealsFoundException when not enough meals are available`() {
        // Given
        val meals = List(4) { createMeal(id = it, ingredients = listOf("Ingredient $it")) }
        every { mealRepository.getAllMeals() } returns meals

        // When&Then
        assertThrows<NotEnoughMealsFoundException> {
            getIngredientGameQuestionsUseCase().getOrThrow()
        }
    }

    @Test
    fun `should return failure with InvalidNumberException when given invalid number of questions`() {
        // Given
        val questionLimit = 1
        val optionsLimit = 3
        val meals =
            List(10) { createMeal(id = it, ingredients = emptyList()) }
        every { mealRepository.getAllMeals() } returns meals

        // When&Then
        assertThrows<InvalidNumberException> {
            getIngredientGameQuestionsUseCase(questionLimit, optionsLimit).getOrThrow()
        }
    }

    @Test
    fun `should return failure with InvalidNumberException when given invalid number of options`() {
        // Given
        val questionLimit = 3
        val optionsLimit = 1
        val meals =
            List(10) { createMeal(id = it, ingredients = emptyList()) }
        every { mealRepository.getAllMeals() } returns meals

        // When&Then
        assertThrows<InvalidNumberException> {
            getIngredientGameQuestionsUseCase(questionLimit, optionsLimit).getOrThrow()
        }
    }

    @Test
    fun `should return failure with NoIngredientFoundException when given meals with no ingredient`() {
        // Given
        val meals =
            List(15) { createMeal(id = it, ingredients = emptyList()) }
        every { mealRepository.getAllMeals() } returns meals

        // When&Then
        assertThrows<NoIngredientFoundException> {
            getIngredientGameQuestionsUseCase().getOrThrow()
        }
    }

    @Test
    fun `should return success result that each question contains exactly one correct answer`() {
        // Given
        val meals = List(10) { createMeal(id = it, ingredients = listOf("Ingredient $it")) }
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getIngredientGameQuestionsUseCase(5, 3)

        // Then
        assertThat(result.isSuccess).isTrue()
        val questions = result.getOrNull()
        assertThat(questions).isNotNull()
        questions?.first()?.let { question ->
            assertThat(question.chooses.size).isEqualTo(3)
            assertThat(question.chooses.count { it.first }).isEqualTo(1)
            assertThat(question.chooses.count { !it.first }).isEqualTo(2)
        }
    }

    @Test
    fun `should return success result with no duplicate questions`() {
        // Given
        val meals = createSampleMeals(20)
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getIngredientGameQuestionsUseCase(10, 3)

        // Then
        assertThat(result.isSuccess).isTrue()
        val questions = result.getOrNull()
        assertThat(questions).isNotNull()
        val questionMealNames = questions?.map { it.id }
        val uniqueQuestionMealNames = questionMealNames?.toSet()
        assertThat(questionMealNames?.size).isEqualTo(uniqueQuestionMealNames?.size)
    }

    @Test
    fun `should return success result with questions that have correct answers are ingredients of the question meal`() {
        // Given
        val meals = List(10) { createMeal(id = it, ingredients = listOf("Ingredient $it")) }
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getIngredientGameQuestionsUseCase(5, 3)

        // Then
        assertThat(result.isSuccess)
        val questions = result.getOrNull()
        assertThat(questions).isNotNull()
        questions?.forEach { question ->
            val mealId = question.id
            val correctAnswer = question.chooses.find { it.first }?.second
            val meal = meals.find { it.id == mealId }
            assertThat(meal).isNotNull()
            assertThat(correctAnswer).isNotNull()
            assertThat(
                meal!!.ingredients.any {
                    it.equals(correctAnswer, ignoreCase = true)
                },
            ).isTrue()
        }
    }

    @Test
    fun `should return success result with questions that have wrong answers are not ingredients of the question meal`() {
        // Given
        val meals = List(10) { createMeal(id = it, ingredients = listOf("Ingredient $it")) }
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getIngredientGameQuestionsUseCase(5, 3)

        // Then
        assertThat(result.isSuccess).isTrue()
        val questions = result.getOrNull()
        assertThat(questions).isNotNull()
        questions?.forEach { question ->
            val mealId = question.id
            val wrongAnswers = question.chooses.filter { !it.first }.map { it.second }
            val meal = meals.find { it.id == mealId }
            assertThat(meal).isNotNull()
            wrongAnswers.forEach { wrongAnswer ->
                assertThat(
                    meal!!.ingredients.any {
                        it.contains(wrongAnswer, ignoreCase = true) ||
                            wrongAnswer.contains(it, ignoreCase = true)
                    },
                ).isFalse()
            }
        }
    }


    @Test
    fun `should return failure with NoIngredientFoundException when wrong options cannot be generated`() {
        // Given
        val meals =
            listOf(
                createMeal(1, "Meal 1", listOf("fish")),
                createMeal(2, "Meal 2", emptyList()),
                createMeal(3, "Meal 3", emptyList()),
                createMeal(4, "Meal 4", emptyList()),
                createMeal(6, "Meal 6", listOf("duck")),
            )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getIngredientGameQuestionsUseCase(3, 3)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThrows<NoIngredientFoundException> { result.getOrThrow() }
    }

    private fun createSampleMeals(count: Int): List<Meal> =
        (1..count).map { i ->
            createMeal(
                id = i,
                name = "Meal $i",
                ingredients =
                    listOf(
                        "Ingredient ${i}A",
                        "Ingredient ${i}B",
                        "Ingredient ${i}C",
                    ),
            )
        }
}
