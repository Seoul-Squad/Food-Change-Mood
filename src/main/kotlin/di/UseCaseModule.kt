package di

import GetItalianLargeMealsUseCase
import logic.useCase.ExploreCountryMealsUseCase
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetKetoDietMealUseCase
import logic.useCase.GetRandomEasyMealsUseCase
import logic.useCase.GetRandomPotatoMealsUseCase
import logic.useCase.GetMealsByCaloriesAndProteinUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.useCase.GuessMealPreparationTimeGameUseCase
import org.koin.dsl.module
import org.seoulsquad.logic.useCase.*
import org.seoulsquad.logic.useCase.GetHealthyFastFoodUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomEasyMealsUseCase(get()) }
        single { GetRandomPotatoMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { SearchMealsByNameUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
        single { ExploreCountryMealsUseCase(get()) }
        single { GetMealsByCaloriesAndProteinUseCase(get()) }
        single { GetSeafoodMealsSortedByProteinUseCase(get()) }
        single { GetKetoDietMealUseCase(get()) }
        single { GetMealsWithHighCaloriesUseCase(get()) }
        single { GetItalianLargeMealsUseCase(get()) }
        single { SearchFoodsUsingDateUseCase(get()) }
        single { GetMealUsingIDUseCase(get()) }
        single { GuessMealPreparationTimeGameUseCase(get()) }
        single { GetHealthyFastFoodUseCase(get()) }
        single { GetRandomIngredientQuestion(get()) }

    }
