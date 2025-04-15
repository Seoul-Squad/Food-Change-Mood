package org.seoulsquad.di

import org.koin.dsl.module
import org.seoulsquad.data.repository.MealRepositoryImpl
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.presentation.ConsoleUi

val repositoryModule = module {
    single<MealRepository> { MealRepositoryImpl(get(), get()) }
    single { ConsoleUi(get()) }
}