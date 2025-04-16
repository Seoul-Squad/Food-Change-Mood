package org.seoulsquad.di

import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.koin.dsl.module
import org.seoulsquad.logic.use_case.GetRandomEasyMealsUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomEasyMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
    }
