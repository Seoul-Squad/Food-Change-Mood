package org.seoulsquad

import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin
import org.seoulsquad.di.csvModule
import org.seoulsquad.di.repositoryModule
import org.seoulsquad.di.useCaseModule
import org.seoulsquad.presentation.ConsoleUi

fun main() {
    startKoin {
        modules(repositoryModule,useCaseModule,csvModule)
    }
    val consoleUi: ConsoleUi =getKoin().get()
    consoleUi.start()

}

