package di

import logic.useCase.ExploreOtherCountriesFoodUseCase
import org.koin.dsl.module
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.useCase.GetRandomPotatoMealsUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomPotatoMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
    }
