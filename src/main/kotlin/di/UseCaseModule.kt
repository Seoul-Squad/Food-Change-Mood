package di

import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.koin.dsl.module
import org.seoulsquad.logic.useCase.IngredientGameUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
        single { IngredientGameUseCase(get()) }
    }
