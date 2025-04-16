package org.seoulsquad

import di.appModule
import di.csvModule
import di.repositoryModule
import di.useCaseModule
import org.koin.core.context.startKoin
import org.seoulsquad.di.csvModule
import org.seoulsquad.di.repositoryModule
import org.seoulsquad.di.useCaseModule

fun main() {
    startKoin {
        modules(appModule,repositoryModule,useCaseModule,csvModule)
    }
    val consoleUi: ConsoleUi =getKoin().get()
    consoleUi.start()

}

