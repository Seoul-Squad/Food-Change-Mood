package org.seoulsquad

import org.koin.core.context.startKoin
import org.seoulsquad.di.appModule
import org.seoulsquad.di.useCaseModule

fun main() {
    startKoin {
        modules(appModule,useCaseModule)
    }

}

