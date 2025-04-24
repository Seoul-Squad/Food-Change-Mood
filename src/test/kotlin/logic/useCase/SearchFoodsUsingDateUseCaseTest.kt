package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import logic.mock_data.createMealForSearchDate
import logic.model.Meal
import logic.utils.InvalidDateException
import logic.utils.NoMealsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.SearchFoodsUsingDateUseCase


class SearchFoodsUsingDateUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var searchFoodsUsingDateUseCase: SearchFoodsUsingDateUseCase
    private lateinit var meals: List<Meal>

    @BeforeEach
    fun setup() {
        mealRepository = mockk(relaxed = true)
        searchFoodsUsingDateUseCase = SearchFoodsUsingDateUseCase(mealRepository)
        meals = listOf(
            createMealForSearchDate(
                1,
                "Grilled Chicken Caesar Salad",
                LocalDate(monthNumber = 9, dayOfMonth = 6, year = 2024)
            ),
            createMealForSearchDate(2, "Spaghetti Bolognese", LocalDate(monthNumber = 4, dayOfMonth = 2, year = 2024)),
            createMealForSearchDate(
                3,
                "Teriyaki Salmon with Steamed Rice",
                LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024)
            ),
            createMealForSearchDate(4, "Vegetable Pad Thai", LocalDate(monthNumber = 6, dayOfMonth = 4, year = 2024)),
            createMealForSearchDate(
                5,
                "Beef Tacos with Pico de Gallo",
                LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024)
            ),
            createMealForSearchDate(6, "Margherita Pizza", LocalDate(monthNumber = 7, dayOfMonth = 20, year = 2024))
        )
    }


    @Test
    fun `should return matching meals when searching for valid date with meals`() {
        every { mealRepository.getAllMeals() } returns meals

        val result = searchFoodsUsingDateUseCase(LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024))

        val mealIds = result.getOrNull()?.map { it.id }
        assertThat(mealIds).isEqualTo(listOf(3, 5))
    }

    @Test
    fun `should throw NoMealsFoundException when searching for valid date with no matching meals`() {
        every { mealRepository.getAllMeals() } returns meals

        val result = searchFoodsUsingDateUseCase(LocalDate(monthNumber = 9, dayOfMonth = 4, year = 2024))

        assertThrows<NoMealsFoundException> { result.getOrThrow() }

    }

    @Test
    fun `should throw InvalidDateException when searching with invalid date`() {
        every { mealRepository.getAllMeals() } returns meals

        val result = searchFoodsUsingDateUseCase(null)

        assertThrows<InvalidDateException> { result.getOrThrow() }
    }


    @ParameterizedTest
    @EmptySource
    fun `should throw NoMealsFoundException when repository returns empty list`(meals: List<Meal>) {
        every { mealRepository.getAllMeals() } returns meals

        val result = searchFoodsUsingDateUseCase(LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024))

        assertThrows<NoMealsFoundException> { result.getOrThrow() }

    }


    @Test
    fun `should throw NoMealsFoundException when repository returns only invalid date with meals`() {
        every { mealRepository.getAllMeals() } returns listOf(
            createMealForSearchDate(1, "Grilled Chicken Caesar Salad", null),
            createMealForSearchDate(2, "Spaghetti Bolognese", null),
            createMealForSearchDate(3, "Teriyaki Salmon with Steamed Rice", null),
            createMealForSearchDate(4, "Vegetable Pad Thai", null),
            createMealForSearchDate(5, "Beef Tacos with Pico de Gallo", null),
            createMealForSearchDate(6, "Margherita Pizza", null)
        )

        val result = searchFoodsUsingDateUseCase(LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024))

        assertThrows<NoMealsFoundException> { result.getOrThrow() }


    }
}

