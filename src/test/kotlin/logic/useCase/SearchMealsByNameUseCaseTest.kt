package logic.useCase

import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import mockData.createSearchByNameMeal
import io.mockk.every
import logic.utils.BlankInputException
import logic.utils.NoMealsFoundException
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.SearchMealsByNameUseCase
import kotlin.test.assertEquals


class SearchMealsByNameUseCaseTest {
    private lateinit var searchMealsByNameUseCase: SearchMealsByNameUseCase
    private lateinit var mealRepository: MealRepository

    @BeforeEach
    fun setup() {
        mealRepository = mockk(relaxed = true)
        searchMealsByNameUseCase = SearchMealsByNameUseCase(mealRepository)
    }

    @Test
    fun `SearchMealsByNameUseCase should return failure when query input is empty`() {
        //Given
        val query = ""
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("hello"),
            createSearchByNameMeal("world")
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(BlankInputException::class.java)
    }

    @Test
    fun `SearchMealsByNameUseCase should return failure when no meal found`() {
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
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `SearchMealsByNameUseCase should return failure when there is no meals`() {
        //Given
        val query = "Most"
        every { mealRepository.getAllMeals() } returns emptyList()
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `SearchMealsByNameUseCase should return failure when query is larger than names`() {
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
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `SearchMealsByNameUseCase should return failure when query has typo`() {
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
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @ParameterizedTest
    @ValueSource(strings = ["hello", "Hello", "HELLO", "heLLo"])
    fun `SearchMealsByNameUseCase should find result in case-insensitive query`(query: String) {
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
        assertEquals(3, result.getOrNull()?.size)
    }

    @Test
    fun `SearchMealsByNameUseCase should return meals when query has repeated charachters `() {
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
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `SearchMealsByNameUseCase should return failure when query has overlapped charachters`() {
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
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `SearchMealsByNameUseCase should return meals when query  matched via substring`() {
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
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `SearchMealsByNameUseCase should return meals when query  mismatch then match`() {
        //Given
        val query = "ababcaba"
        every { mealRepository.getAllMeals() } returns listOf(
            createSearchByNameMeal("mababnabmoonababcaba Smoothie"),
        )
        //When
        val result = searchMealsByNameUseCase(query)
        //Then
        assertThat(result.isSuccess).isTrue()
        assertEquals(1, result.getOrNull()?.size)
    }
}