package org.seoulsquad.di

import org.koin.dsl.module
import logic.useCase.GetAllMealsUseCase

val useCaseModule= module {
    single { GetAllMealsUseCase(get()) }
    single { ExploreOtherCountriesFoodUseCase(get())}
}
