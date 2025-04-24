package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import logic.model.Meal
import logic.utils.InvalidIdException
import logic.utils.NoMealsFoundException
import mockData.createMealForSearchDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.seoulsquad.logic.model.MealDate
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.GetMealUsingIDUseCase

class GetMealUsingIDUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var getMealUsingIDUseCase: GetMealUsingIDUseCase
    private lateinit var mealsDate: List<MealDate>
    private lateinit var meals: List<Meal>

    @BeforeEach
    fun setup() {
        mealRepository = mockk(relaxed = true)
        getMealUsingIDUseCase = GetMealUsingIDUseCase(mealRepository)
        mealsDate = listOf(
            MealDate(1, "Grilled Chicken Caesar Salad", LocalDate(monthNumber = 9, dayOfMonth = 6, year = 2024)),
            MealDate(2, "Spaghetti Bolognese", LocalDate(monthNumber = 4, dayOfMonth = 2, year = 2024)),
            MealDate(3, "Teriyaki Salmon with Steamed Rice", LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024)),
            MealDate(4, "Vegetable Pad Thai", LocalDate(monthNumber = 6, dayOfMonth = 4, year = 2024)),
            MealDate(5, "Beef Tacos with Pico de Gallo", LocalDate(monthNumber = 4, dayOfMonth = 4, year = 2024)),
            MealDate(6, "Margherita Pizza", LocalDate(monthNumber = 7, dayOfMonth = 20, year = 2024))
        )
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
    fun `should return the matching meal when searching with a valid and existing ID from the list`() {
        every { mealRepository.getAllMeals() } returns meals

        val result = getMealUsingIDUseCase(3, mealsDate)

        assertThat(result.getOrNull()?.id).isEqualTo(3)
    }

    @Test
    fun `should throw a NoMealsFoundException when searching with a valid but non-existent ID from the list`() {
        every { mealRepository.getAllMeals() } returns meals

        val result = getMealUsingIDUseCase(99, mealsDate)

        assertThrows<NoMealsFoundException> { result.getOrThrow() }


    }

    @Test
    fun `should throw a InvalidIdException when searching with null ID`() {
        every { mealRepository.getAllMeals() } returns meals

        val result = getMealUsingIDUseCase(null, mealsDate)

        assertThrows<InvalidIdException> { result.getOrThrow() }


    }


    @Test
    fun `should throw NoMealsFoundException when empty list return form repository`() {
        every { mealRepository.getAllMeals() } returns emptyList()

        val result = getMealUsingIDUseCase(2, mealsDate)

        assertThrows<NoMealsFoundException> { result.getOrThrow() }

    }


    @Test
    fun `should throw NoMealsFoundException when searching in an empty meal list `() {
        every { mealRepository.getAllMeals() } returns meals

        val result = getMealUsingIDUseCase(2, emptyList())

        assertThrows<NoMealsFoundException> { result.getOrThrow() }


    }


    @Test
    fun `should throw InvalidIdException when searching with an negative meal ID`() {
        every { mealRepository.getAllMeals() } returns meals

        val result = getMealUsingIDUseCase(-1, emptyList())

        assertThrows<InvalidIdException> { result.getOrThrow() }

    }


}