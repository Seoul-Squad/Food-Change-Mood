package logic.useCase

import logic.model.Meal
import logic.utils.BlankInputException
import logic.utils.NoMealsFoundException
import org.seoulsquad.logic.repository.MealRepository


class ExploreCountryMealsUseCase(
    private val mealRepository: MealRepository
) {
    operator fun invoke(countryName: String, limit: Int = DEFAULT_LIMIT): Result<List<Meal>> {
        if (isCountryNameBlank(countryName)) return Result.failure(BlankInputException())

        val meals = mealRepository
            .getAllMeals()
            .filter { isMealFromCountry(it, countryName) }
            .shuffled()
            .take(limit)

        return meals
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: Result.failure(NoMealsFoundException())
    }

    private fun isMealFromCountry(meal: Meal, countryName: String) =
        meal.name.contains(countryName, ignoreCase = true) || meal.description?.contains(
            countryName, ignoreCase = true
        ) ?: false || meal.tags.contains(countryName)

    private fun isCountryNameBlank(countryName: String) = (countryName.isBlank())


    companion object {
        const val DEFAULT_LIMIT = 20
    }
}
