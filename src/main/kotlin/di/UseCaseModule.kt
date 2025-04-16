package di

import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetRandomEasyMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import org.seoulsquad.logic.useCase.GetSearchByNameUseCase
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomEasyMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { GetSearchByNameUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
    }
