package logic.useCase

import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import mockData.createSearchByNameMeal
import io.mockk.every
import logic.utils.BlankInputException
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.SearchMealsByNameUseCase

class SearchMealsByNameUseCaseTest {
    private lateinit var searchMealsByNameUseCase: SearchMealsByNameUseCase
    private lateinit var mealRepository: MealRepository

    @BeforeEach
    fun setup() {
        mealRepository = mockk(relaxed = true)
        searchMealsByNameUseCase = SearchMealsByNameUseCase(mealRepository)
    }

    @Test
    fun `should return failure BlankInputException when query is empty`() {
        //Given
        val query = ""
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("hello"),
            createSearchByNameMeal("world")
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThrows<BlankInputException> { result.getOrThrow() }
    }

    @Test
    fun `should return failure NoMealsFoundException query don't match any meal`() {
        //Given
        val query = "Most"
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("hello"),
            createSearchByNameMeal("world"),
            createSearchByNameMeal("hello world"),
            createSearchByNameMeal("world hello"),
            createSearchByNameMeal("world wold wold"),
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }

    @Test
    fun `should return failure NoMealsFoundException when there is no meals`() {
        //Given
        val query = "Most"
        every { mealRepository.getAllMeals() } returns emptyList()
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }

    @Test
    fun `should return failure NoMealsFoundException when query is larger than any meal's name`() {
        //Given
        val query = "chocolate cake dessert sweet"
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("cake"),
            createSearchByNameMeal("chocolate cake"),
            createSearchByNameMeal("sweet"),
            createSearchByNameMeal("cake dessert"),
            createSearchByNameMeal("dessert sweet"),
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }

    @Test
    fun `should return failure NoMealsFoundException when query has typo`() {
        //Given
        val query = "kushari"
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("kushary  meal"),
            createSearchByNameMeal("koshari Medo"),
            createSearchByNameMeal("koshare Hamedo"),
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }

    @ParameterizedTest
    @ValueSource(strings = ["hello", "Hello", "HELLO", "heLLo"])
    fun `should find result in case-insensitive query`(query: String) {
        //Given
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("hello"),
            createSearchByNameMeal("world"),
            createSearchByNameMeal("hello world"),
            createSearchByNameMeal("world hello"),
            createSearchByNameMeal("world wold wold"),
            createSearchByNameMeal(""),
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.all { it.name.lowercase().contains(query.lowercase()) }).isTrue()
    }

    @Test
    fun `should find result when query has repeated charachters `() {
        //Given
        val query = "aaa"
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("Aaaab Sandwich"),
            createSearchByNameMeal("caake dessert"),
            createSearchByNameMeal("desaasert sweet"),
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.all { it.name.contains(query) }).isTrue()
    }

    @Test
    fun `should find result when query has overlapped charachters`() {
        //Given
        val query = "abcaby"
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("Abcabcaby Salad"),
            createSearchByNameMeal("caake dessert"),
            createSearchByNameMeal("Abcabcaby sweet"),
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.all { it.name.contains(query) }).isTrue()
    }

    @Test
    fun `should find result when query is subset of any meal's name`() {
        //Given
        val query = "berr"
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("Blueberry Smoothie"),
            createSearchByNameMeal("caake dessert"),
            createSearchByNameMeal("Aaacaaab  Blueberry"),
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.all { it.name.contains(query) }).isTrue()
    }

    @Test
    fun `should trigger backtrack when partial match is followed by mismatch`() {
        // Given
        val query = "ababd"
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("ababx")
        )
        // When
        val result = searchMealsByNameUseCase(query)
        // Then
        assertThrows<NoMealsFoundException> { result.getOrThrow() }
    }
    @Test
    fun `should find result when query mismatch requires meal's name shift `() {
        val query = "AAAAB"
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("prefix_AAAAAB_suffix"),
            createSearchByNameMeal("AAAAA"),
            createSearchByNameMeal("AAAB"),
            createSearchByNameMeal("Another meal")
        )

        // When
        val result = searchMealsByNameUseCase(query)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).containsExactly(createSearchByNameMeal("prefix_AAAAAB_suffix"))
    }

}