package di

import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.useCase.GetIraqiMealsUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
    }

