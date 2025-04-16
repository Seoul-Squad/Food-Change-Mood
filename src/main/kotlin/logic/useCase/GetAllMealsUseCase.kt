package logic.useCase

import logic.model.Meal
import logic.repository.MealRepository


class GetAllMealsUseCase(private val repository: MealRepository) {
    operator fun invoke(): List<Meal> {
        return repository.getAllMeals()
    }
}