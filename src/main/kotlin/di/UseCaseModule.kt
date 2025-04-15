package org.seoulsquad.di

import org.koin.dsl.module
import org.seoulsquad.logic.use_case.GetTenRandomEasyMealsUseCase
import org.seoulsquad.logic.use_case.GetAllMealsUseCase

val useCaseModule= module {
    single { GetAllMealsUseCase(get()) }
    single { GetTenRandomEasyMealsUseCase(get()) }
}
