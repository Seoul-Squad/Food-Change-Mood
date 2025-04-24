package org.seoulsquad.presentation.di

import org.seoulsquad.presentation.*

data class PresentationDependencies(
    val searchByNameConsole: SearchMealByNameUi,
    val iraqiMealsUi: IraqiMealsUi,
    val randomEasyMealsUi: RandomEasyMealsUi,
    val guessMealPreparationTimeGameUI: GuessMealPreparationTimeGameUI,
    val sweetsWithNoEggsConsole: SweetsWithNoEggsUi,
    val ketoDietMealsUi: KetoDietMealsUi,
    val searchMealUsingDateUi: SearchMealUsingDateUi,
    val exploreOtherCountriesFoodConsole: ExploreOtherCountriesFoodUi,
    val ingredientGameUi: IngredientGameUi,
    val showRandomMealsByIngredientUi: ShowRandomMealsByIngredientUi,
    val mealsWithHighCaloriesUi: MealsWithHighCaloriesUi,
    val seaFoodMealsSortedByProteinConsole: SeaFoodMealsSortedByProteinUi,
    val healthyMealUi: HealthyMealUi,
    val italianLargeMealsConsole: ItalianLargeMealsUi,
    val mealsByCaloriesAndProteinUi: MealsByCaloriesAndProteinUi,
)
