package di

import org.koin.dsl.module
import presentation.ConsoleUi

val appModule =
    module {
        single { ConsoleUi(get(),get()) }
    }
