package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.seoulsquad.presentation.ExploreOtherCountriesFoodUi
import org.seoulsquad.presentation.GuessGameUi
import org.seoulsquad.presentation.IngredientGame
import org.seoulsquad.presentation.IraqiMealsUi
import org.seoulsquad.presentation.ItalianLargeMealsUi
import org.seoulsquad.presentation.KetoDietMealsUi
import org.seoulsquad.presentation.MealsWithHighCaloriesUi
import org.seoulsquad.presentation.RandomEasyMealsUi
import org.seoulsquad.presentation.SeaFoodMealsSortedByProteinUi
import org.seoulsquad.presentation.SearchByNameUi
import org.seoulsquad.presentation.SearchMealUsingDateUi
import org.seoulsquad.presentation.ShowRandomPotatoMealsUi
import org.seoulsquad.presentation.SweetsWithNoEggsUi
import presentation.ConsoleUi

val appModule =
    module {
        singleOf(::ConsoleUi)
        singleOf(::ExploreOtherCountriesFoodUi)
        singleOf(::GuessGameUi)
        singleOf(::IraqiMealsUi)
        singleOf(::ItalianLargeMealsUi)
        singleOf(::KetoDietMealsUi)
        singleOf(::MealsWithHighCaloriesUi)
        singleOf(::RandomEasyMealsUi)
        singleOf(::SeaFoodMealsSortedByProteinUi)
        singleOf(::SearchByNameUi)
        singleOf(::SearchMealUsingDateUi)
        singleOf(::ShowRandomPotatoMealsUi)
        singleOf(::SweetsWithNoEggsUi)
        singleOf(::IngredientGame)
    }
