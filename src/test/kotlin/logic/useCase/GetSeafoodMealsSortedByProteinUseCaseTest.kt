package logic.useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.model.Meal
import logic.utils.Constants.Tags.TAG_SEAFOOD
import logic.utils.NoMealsFoundException
import utils.createMeal
import utils.createNutritionWithProtein
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.useCase.GetSeafoodMealsSortedByProteinUseCase
import java.util.stream.Stream

class GetSeafoodMealsSortedByProteinUseCaseTest {
    private lateinit var mealsRepo: MealRepository
    private lateinit var getSeafoodMealsSortedByProteinUseCase: GetSeafoodMealsSortedByProteinUseCase

    @BeforeEach
    fun setup() {
        mealsRepo = mockk(relaxed = true)
        getSeafoodMealsSortedByProteinUseCase = GetSeafoodMealsSortedByProteinUseCase(mealsRepo)
    }

    @ParameterizedTest
    @MethodSource("provideSuccessScenarios")
    fun `success scenarios`(
        meals: List<Meal>,
        expectedOrderedIDs: List<Int>,
    ){
        //Given
        every { mealsRepo.getAllMeals() } returns meals

        //When
        val actualOrderedIDs = getSeafoodMealsSortedByProteinUseCase().getOrNull()?.map { it.id }

        //Then
        assertThat(actualOrderedIDs).containsExactlyElementsIn(expectedOrderedIDs).inOrder()
    }


    @ParameterizedTest
    @MethodSource("provideFailureScenarios")
    fun `failure scenarios`(
        meals: List<Meal>
    ){
        //Given
        every { mealsRepo.getAllMeals() } returns meals

        //When
        val result = getSeafoodMealsSortedByProteinUseCase()

        //Then
        assertThrows<NoMealsFoundException> {
            result.getOrThrow()
        }
    }

    companion object{
        @JvmStatic
        fun provideSuccessScenarios(): Stream<Arguments>  = Stream.of(
            Arguments.argumentSet(
                "should return list with sorted seafood meals when they are available",
                listOf(
                    createMeal(id = 1, name = "Salmon", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 35.0)),
                    createMeal(id = 2, name = "Shrimp", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 20.0)),
                    createMeal(id = 3, name = "Tuna", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 30.0)),
                    createMeal(id = 4, name = "Noodles", tags = listOf("japanese"), nutrition = createNutritionWithProtein(protein = 25.0))
                ),
                listOf(1, 3, 2)
            ),
            Arguments.argumentSet(
                "should return list with sorted seafood meals ignoring tag case when they are available",
                listOf(
                    createMeal(id = 1, name = "Shrimp", tags = listOf("SEAFOOD"), nutrition = createNutritionWithProtein(protein = 20.0)),
                    createMeal(id = 2, name = "Tuna", tags = listOf("Seafood"), nutrition = createNutritionWithProtein(protein = 30.0)),
                    createMeal(id = 3, name = "Noodles", tags = listOf("japanese"), nutrition = createNutritionWithProtein(protein = 25.0))
                ),
                listOf(2, 1)
            ),
            Arguments.argumentSet(
                "should return seafood meals sorted by name when protein content is equal",
                listOf(
                    createMeal(id = 1, name = "Tuna", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 30.0)),
                    createMeal(id = 2, name = "Shrimp", tags = listOf("seafood"), nutrition = createNutritionWithProtein(protein = 30.0)),
                    createMeal(id = 3, name = "Koshari", tags = listOf("egyptian"), nutrition = createNutritionWithProtein(5.0))
                ),
                listOf(2, 1)
            )
        )
        @JvmStatic
        fun provideFailureScenarios(): Stream<Arguments> = Stream.of(
            Arguments.argumentSet(
                "should return failure result with NoMealsFoundException when there is no seafood meals",
                listOf(
                    createMeal(id = 1, name = "Noodles", tags = listOf("japanese"), nutrition = createNutritionWithProtein(protein = 25.0)),
                    createMeal(id = 2, name = "Koshari", tags = listOf("egyptian"), nutrition = createNutritionWithProtein(5.0))
                )
            ),
            Arguments.argumentSet(
                "should return failure result with NoMealsFoundException when meals are empty",
                emptyList<Meal>()
            )
        )
    }
}