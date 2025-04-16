package org.seoulsquad.di

import org.koin.dsl.module
import org.seoulsquad.logic.use_case.GetAllMealsUseCase
import org.seoulsquad.logic.use_case.GuessGameUseCase

val useCaseModule= module {
    single { GetAllMealsUseCase(get()) }
    single { GuessGameUseCase(get()) }
}
