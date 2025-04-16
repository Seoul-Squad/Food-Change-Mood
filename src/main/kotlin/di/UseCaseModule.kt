package di

import logic.useCase.ExploreOtherCountriesFoodUseCase
import org.koin.dsl.module
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import logic.useCase.GuessGameUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
        single { GuessGameUseCase(get()) }
    }


