package di

import logic.useCase.ExploreOtherCountriesFoodUseCase
import org.koin.dsl.module
import logic.useCase.GetAllMealsUseCase
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
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { GetSearchByNameUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
        single { GetAllMealsUseCase(get()) }
        single { SearchFoodsUsingDateUseCase(get()) }
        single { GetMealUsingIDUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }

        single { GuessGameUseCase(get()) }
    }


