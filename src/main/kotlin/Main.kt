package org.seoulsquad

import di.appModule
import di.csvModule
import di.repositoryModule
import org.koin.core.context.startKoin
import org.seoulsquad.di.useCaseModule

fun main() {
    startKoin {
        modules(appModule,repositoryModule,useCaseModule,csvModule)
    }

}

