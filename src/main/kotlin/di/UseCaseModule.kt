package di

import logic.useCase.ExploreOtherCountriesFoodUseCase
import org.koin.dsl.module
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetMealsByCaloriesAndProtein
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
        single { GetMealsByCaloriesAndProtein(get()) }
    }

