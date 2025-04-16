package di


import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.ConsoleUi

val appModule =
    module {
        singleOf(::ConsoleUi)
    }
