package org.seoulsquad.logic.use_case

import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.utils.Constants.POTATO_ONLY
import org.seoulsquad.model.Meal
import kotlin.random.Random

class GetTenRandomPotatoMealsUseCase(
    private val repository: MealRepository,
) {
    operator fun invoke(): List<Meal> {
        val mealsContainPotato = repository.getAllMeals().filter(::onlyWithPotato)
        return List(10) {
            val randomMealIndex = Random.nextInt(until = mealsContainPotato.size)

            mealsContainPotato[randomMealIndex]
        }
    }

    private fun onlyWithPotato(meal: Meal): Boolean = meal.ingredients.contains(POTATO_ONLY)
}
