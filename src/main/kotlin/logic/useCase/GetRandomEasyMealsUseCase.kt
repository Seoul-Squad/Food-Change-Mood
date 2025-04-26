package logic.useCase

import logic.model.Meal
import logic.utils.Constants.EasyFood.MAX_INGREDIENTS
import org.seoulsquad.logic.repository.MealRepository
import logic.utils.Constants.EasyFood.MAX_MINUTES
import logic.utils.Constants.EasyFood.MAX_STEPS
import logic.utils.NoMealsFoundException

class GetRandomEasyMealsUseCase(private val mealRepository: MealRepository) {

    operator fun invoke(limit:Int = DEFAULT_LIMIT): Result<List<Meal>> {
        val easyMealsList =
            mealRepository.getAllMeals().filter(::isEasyMeal)

        return easyMealsList
            .shuffled()
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(easyMealsList.take(limit)) }
            ?: Result.failure(NoMealsFoundException())
    }

    private fun isEasyMeal(meal: Meal): Boolean =
        meal.preparationTimeInMinutes <= MAX_MINUTES &&
        meal.ingredients.size <= MAX_INGREDIENTS &&
        meal.numberOfSteps <= MAX_STEPS


    companion object{
        const val DEFAULT_LIMIT = 10
    }
}