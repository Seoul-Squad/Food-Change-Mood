package di

import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetRandomEasyMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomEasyMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
    }
