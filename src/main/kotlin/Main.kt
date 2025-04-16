package org.seoulsquad

import di.appModule
import di.csvModule
import di.repositoryModule
import di.useCaseModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.ConsoleUi

fun main() {
    startKoin {
        modules(appModule,repositoryModule,useCaseModule,csvModule)
    }
    val consoleUi: ConsoleUi =getKoin().get()
    consoleUi.start()

}

