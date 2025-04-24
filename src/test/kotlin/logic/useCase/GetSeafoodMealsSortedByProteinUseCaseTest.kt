package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.utils.Constants.Tags.TAG_SEAFOOD
import logic.utils.NoMealsFoundException
import utils.createMeal
import utils.createNutritionWithProtein
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.GetSeafoodMealsSortedByProteinUseCase

class GetSeafoodMealsSortedByProteinUseCaseTest {
    private lateinit var mealsRepo: MealRepository
    private lateinit var getSeafoodMealsSortedByProteinUseCase: GetSeafoodMealsSortedByProteinUseCase

    @BeforeEach
    fun setup() {
        mealsRepo = mockk(relaxed = true)
        getSeafoodMealsSortedByProteinUseCase = GetSeafoodMealsSortedByProteinUseCase(mealsRepo)
    }

    @Test
    fun `should return list with sorted seafood meals when they are available`(){
        //Given
        val meals = listOf(
            createMeal(name = "Salmon", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 35.0)),
            createMeal(name = "Shrimp", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 20.0)),
            createMeal(name = "Tuna", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 30.0)),
            createMeal(name = "Noodles", tags = listOf("japanese"), nutrition = createNutritionWithProtein(protein = 25.0))
        )
        every { mealsRepo.getAllMeals() } returns meals

        //When
        val result = getSeafoodMealsSortedByProteinUseCase()

        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()?.size).isEqualTo(3)
        assertThat(result.getOrNull()?.get(0)?.name).isEqualTo("Salmon")
        assertThat(result.getOrNull()?.get(1)?.name).isEqualTo("Tuna")
        assertThat(result.getOrNull()?.get(2)?.name).isEqualTo("Shrimp")
    }

    @Test
    fun `should return list with sorted seafood meals ignoring tag case when they are available`(){
        //Given
        val meals = listOf(
            createMeal(name = "Shrimp", tags = listOf("SEAFOOD"), nutrition = createNutritionWithProtein(protein = 20.0)),
            createMeal(name = "Tuna", tags = listOf("Seafood"), nutrition = createNutritionWithProtein(protein = 30.0)),
            createMeal(name = "Noodles", tags = listOf("japanese"), nutrition = createNutritionWithProtein(protein = 25.0))
        )
        every { mealsRepo.getAllMeals() } returns meals

        //When
        val result = getSeafoodMealsSortedByProteinUseCase()

        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()?.size).isEqualTo(2)
        assertThat(result.getOrNull()?.get(0)?.name).isEqualTo("Tuna")
        assertThat(result.getOrNull()?.get(1)?.name).isEqualTo("Shrimp")
    }

    @Test
    fun `should return seafood meals sorted by name when protein content is equal`(){
        //Given
        val meals = listOf(
            createMeal(name = "Tuna", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 30.0)),
            createMeal(name = "Shrimp", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 30.0)),
            createMeal(name = "Koshari", tags = listOf("egyptian"), nutrition = createNutritionWithProtein(5.0))
        )
        every { mealsRepo.getAllMeals() } returns meals

        //When
        val result = getSeafoodMealsSortedByProteinUseCase()

        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()?.size).isEqualTo(2)
        assertThat(result.getOrNull()?.get(0)?.name).isEqualTo("Shrimp")
        assertThat(result.getOrNull()?.get(1)?.name).isEqualTo("Tuna")
    }
    @Test
    fun `should return failure result with NoMealsFoundException when there is no seafood meals`(){
        //Given
        val meals = listOf(
            createMeal(name = "Noodles", tags = listOf("japanese"), nutrition = createNutritionWithProtein(protein = 25.0)),
            createMeal(name = "Koshari", tags = listOf("egyptian"), nutrition = createNutritionWithProtein(5.0))
        )
        every { mealsRepo.getAllMeals() } returns meals

        //When
        val result = getSeafoodMealsSortedByProteinUseCase()

        //Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }

    @Test
    fun `should return failure result with NoMealsFoundException when meals are empty`(){
        //Given
        every { mealsRepo.getAllMeals() } returns emptyList()

        //When
        val result = getSeafoodMealsSortedByProteinUseCase()

        //Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoMealsFoundException::class.java)
    }
}