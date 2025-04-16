package di

import GetItalianLargeMealsUseCase
import logic.useCase.ExploreOtherCountriesFoodUseCase
import org.koin.dsl.module
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetRandomEasyMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.koin.dsl.module
import org.seoulsquad.logic.useCase.GetMealUsingIDUseCase
import org.seoulsquad.logic.useCase.SearchFoodsUsingDateUseCase
import org.seoulsquad.logic.useCase.GetSearchByNameUseCase
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import logic.useCase.GuessGameUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomEasyMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { GetSearchByNameUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
        single { GetItalianLargeMealsUseCase(get()) }
        single { SearchFoodsUsingDateUseCase(get()) }
        single { GetMealUsingIDUseCase(get()) }
        single { GuessGameUseCase(get()) }
    }
