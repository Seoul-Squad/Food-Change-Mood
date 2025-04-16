package org.seoulsquad

import di.appModule
import di.csvModule
import di.repositoryModule
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin
import org.seoulsquad.di.useCaseModule
import org.koin.java.KoinJavaComponent.getKoin
import presentation.ConsoleUi

fun main() {
    startKoin {
        modules(appModule,repositoryModule,useCaseModule,csvModule)
    }
    val consoleUi: ConsoleUi =getKoin().get()
    consoleUi.start()

}

