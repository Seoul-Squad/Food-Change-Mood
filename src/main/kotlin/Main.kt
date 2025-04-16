package org.seoulsquad

import di.appModule
import di.csvModule
import di.repositoryModule
import di.useCaseModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(appModule,repositoryModule,useCaseModule,csvModule)
    }



}

