package di

import GetItalianLargeMealsUseCase
import logic.useCase.ExploreOtherCountriesFoodUseCase
import org.koin.dsl.module
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.koin.dsl.module
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
        single { GetItalianLargeMealsUseCase(get()) }
    }
