package di

import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
    }
