package di

import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.koin.dsl.module
import org.seoulsquad.logic.useCase.GetSearchByNameUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { GetSearchByNameUseCase(get()) }
    }
