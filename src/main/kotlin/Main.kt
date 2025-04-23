package org.seoulsquad

import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import org.seoulsquad.di.appModule
import presentation.ConsoleUi

fun main() {
    startKoin {
        modules(appModule)
    }

    println("Loading, Please wait...")
    val consoleUi: ConsoleUi = getKoin().get()
    consoleUi.start()
}
