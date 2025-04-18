package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.seoulsquad.presentation.*
import presentation.ConsoleUi

val appModule =
    module {
        singleOf(::ConsoleUi)
        singleOf(::ExploreOtherCountriesFoodUi)
        singleOf(::GuessMealPreparationTimeGameUI)
        singleOf(::IraqiMealsUi)
        singleOf(::ItalianLargeMealsUi)
        singleOf(::KetoDietMealsUi)
        singleOf(::MealsWithHighCaloriesUi)
        singleOf(::RandomEasyMealsUi)
        singleOf(::SeaFoodMealsSortedByProteinUi)
        singleOf(::SearchMealByNameUi)
        singleOf(::SearchMealUsingDateUi)
        singleOf(::ShowRandomPotatoMealsUi)
        singleOf(::SweetsWithNoEggsUi)
        singleOf(::MealsByCaloriesAndProteinUi)
        singleOf(::IngredientGameUi)
        singleOf(::HealthyMealUi)
    }
