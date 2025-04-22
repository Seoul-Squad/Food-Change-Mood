package di

import logic.useCase.*
import org.koin.dsl.module
import org.seoulsquad.logic.useCase.*

val useCaseModule =
    module {
        single { GetAllMealsUseCase(get()) }
        single { GetRandomEasyMealsUseCase(get()) }
        single { GetRandomPotatoMealsUseCase(get()) }
        single { GetSweetsWithNoEggsUseCase(get()) }
        single { SearchMealsByNameUseCase(get()) }
        single { GetIraqiMealsUseCase(get()) }
        single { ExploreCountryMealsUseCase(get()) }
        single { GetMealsByCaloriesAndProteinUseCase(get()) }
        single { GetSeafoodMealsSortedByProteinUseCase(get()) }
        single { GetKetoDietMealUseCase(get()) }
        single { GetMealsWithHighCaloriesUseCase(get()) }
        single { GetItalianLargeMealsUseCase(get()) }
        single { SearchFoodsUsingDateUseCase(get()) }
        single { GetMealUsingIDUseCase(get()) }
        single { GuessMealPreparationTimeGameUseCase(get()) }
        single { GetHealthyFastFoodUseCase(get()) }
        single { IngredientGameUseCase(get()) }
    }
