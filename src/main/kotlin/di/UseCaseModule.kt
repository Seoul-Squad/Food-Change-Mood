package di

import GetItalianLargeMealsUseCase
import logic.useCase.*
import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetKetoDietMealUseCase
import logic.useCase.GetRandomEasyMealsUseCase
import logic.useCase.GetRandomPotatoMealsUseCase
import logic.useCase.GetMealsByCaloriesAndProteinUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.useCase.GuessGameUseCase
import org.koin.dsl.module
import org.seoulsquad.logic.useCase.*
import org.seoulsquad.logic.useCase.GetHealthyFastFoodUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomEasyMealsUseCase(get()) }
        single { GetRandomPotatoMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { GetSearchByNameUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
        single { GetMealsByCaloriesAndProteinUseCase(get()) }
        single { GetSortedSeafoodMealsUseCase(get()) }
        single { GetKetoDietMealUseCase(get()) }
        single { GetMealsWithHighCaloriesUseCase(get()) }
        single { GetItalianLargeMealsUseCase(get()) }
        single { SearchFoodsUsingDateUseCase(get()) }
        single { GetMealUsingIDUseCase(get()) }
        single { GuessGameUseCase(get()) }
        single { GetHealthyFastFoodUseCase(get()) }
        single { GetRandomIngredientQuestion(get()) }
    }
