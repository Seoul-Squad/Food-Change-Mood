package di

import GetItalianLargeMealsUseCase
import logic.useCase.ExploreOtherCountriesFoodUseCase
import logic.useCase.GetAllMealsUseCase
import logic.useCase.GetKetoDietMealUseCase
import logic.useCase.GetRandomEasyMealsUseCase
import logic.useCase.GetRandomPotatoMealsUseCase
import logic.useCase.GetSweetsWithNoEggsUseCase
import logic.useCase.GuessGameUseCase
import org.koin.dsl.module
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import org.seoulsquad.logic.useCase.GetMealUsingIDUseCase
import org.seoulsquad.logic.useCase.GetMealsWithHighCaloriesUseCase
import org.seoulsquad.logic.useCase.GetSearchByNameUseCase
import org.seoulsquad.logic.useCase.GetSortedSeafoodMealsUseCase
import org.seoulsquad.logic.useCase.SearchFoodsUsingDateUseCase
import org.seoulsquad.logic.useCase.GetMealsWithHighCaloriesUseCase
import org.seoulsquad.logic.useCase.GetIraqiMealsUseCase
import logic.useCase.GuessGameUseCase
import org.seoulsquad.logic.useCase.IngredientGameUseCase

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomEasyMealsUseCase(get()) }
        single { GetRandomPotatoMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { GetSearchByNameUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
        single { ExploreOtherCountriesFoodUseCase(get()) }
        single { GetSortedSeafoodMealsUseCase(get()) }
        single { GetKetoDietMealUseCase(get()) }
        single { GetMealsWithHighCaloriesUseCase(get()) }
        single { GetItalianLargeMealsUseCase(get()) }
        single { SearchFoodsUsingDateUseCase(get()) }
        single { GetMealUsingIDUseCase(get()) }
        single { GuessGameUseCase(get()) }
        single { IngredientGameUseCase(get()) }
    }
