package di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.seoulsquad.presentation.*
import org.seoulsquad.presentation.consolelIO.ConsoleReader
import org.seoulsquad.presentation.consolelIO.ConsoleViewer
import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer
import org.seoulsquad.presentation.di.MealUiControllers
import org.seoulsquad.presentation.utils.MealPrinter
import org.seoulsquad.presentation.utils.TablePrinter
import presentation.ConsoleUi

val uiModule =
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
        singleOf(::ShowRandomMealsByIngredientUi)
        singleOf(::SweetsWithNoEggsUi)
        singleOf(::MealsByCaloriesAndProteinUi)
        singleOf(::IngredientGameUi)
        singleOf(::HealthyMealUi)
        singleOf(::MealUiControllers)
        singleOf(::MealPrinter)
        singleOf(::TablePrinter)


        singleOf(::ConsoleReader) { bind<Reader>() }
        singleOf(::ConsoleViewer) { bind<Viewer>() }
    }
