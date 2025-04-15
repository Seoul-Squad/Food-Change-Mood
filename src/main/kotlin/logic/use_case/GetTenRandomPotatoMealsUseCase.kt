package org.seoulsquad.logic.use_case

import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.Constants.POTATO_ONLY
import org.seoulsquad.model.Meal

class GetTenRandomPotatoMealsUseCase(
    private val mealRepository: MealRepository,
) {
    operator fun invoke(): List<Meal> {
        val mealsContainPotato = mealRepository.getAllMeals().filter(::onlyWithPotato)
        return List(10) {
            mealsContainPotato.random()
        }
    }

    private fun onlyWithPotato(meal: Meal): Boolean = meal.ingredients.contains(POTATO_ONLY)
}
