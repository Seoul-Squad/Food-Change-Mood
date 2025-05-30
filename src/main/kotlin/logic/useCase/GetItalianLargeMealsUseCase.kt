package logic.useCase

import logic.model.Meal
import logic.utils.Constants
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository

class GetItalianLargeMealsUseCase(
    private val mealRepository: MealRepository
) {
    operator fun invoke(): Result<List<Meal>> {
        return mealRepository.getAllMeals()
            .filter(::isItalianLargeMeal)
            .takeIf { it.isNotEmpty() }
            ?.let {
                Result.success(it)
            } ?: Result.failure(NoMealsFoundException())
    }

    private fun isItalianLargeMeal(meal: Meal) =
        (meal.tags.any { it.equals(Constants.ITALIAN_NAME, ignoreCase = true) } ||
                meal.description?.contains(Constants.ITALIAN_NAME, ignoreCase = true) ?: false) &&
                (meal.tags.any { it.equals(Constants.LARGE_GROUP_NAME, ignoreCase = true) })

}