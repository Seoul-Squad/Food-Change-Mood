package org.seoulsquad.di

import logic.useCase.GetSweetsWithNoEggsUseCase
import org.koin.dsl.module
import logic.useCase.GetRandomEasyMealsUseCase
import logic.useCase.GetAllMealsUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomEasyMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
    }
