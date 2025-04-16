package di

import org.koin.dsl.module
import org.seoulsquad.data.repository.MealRepositoryImpl
import org.seoulsquad.logic.repository.MealRepository


val repositoryModule = module {
    single<MealRepository> { MealRepositoryImpl(get(), get()) }
}