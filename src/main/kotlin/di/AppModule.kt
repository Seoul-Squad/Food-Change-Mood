package di


import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.seoulsquad.presentation.IngredientGame
import presentation.ConsoleUi

val appModule =
    module {
        singleOf(::ConsoleUi)
        singleOf(::IngredientGame)
    }
