package di

import data.repository.MealRepositoryImpl
import logic.repository.MealRepository
import org.koin.dsl.module


val repositoryModule = module {
    single<MealRepository> { MealRepositoryImpl(get(), get()) }
}