package di

import logic.useCase.ExploreOtherCountriesFoodUseCase
import org.koin.dsl.module
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.useCase.GetKetoDietMealUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
        single { GetKetoDietMealUseCase(get()) }
    }
