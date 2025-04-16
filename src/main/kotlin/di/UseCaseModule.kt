package org.seoulsquad.di

import org.koin.dsl.module
import org.seoulsquad.logic.use_case.GetAllMealsUseCase
import org.seoulsquad.logic.use_case.GetKetoDietMealUseCase

val useCaseModule= module {
    single { GetAllMealsUseCase(get()) }

    single { GetKetoDietMealUseCase(get()) }
}
