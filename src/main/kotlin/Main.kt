package org.seoulsquad

import org.koin.core.context.startKoin
import org.seoulsquad.di.csvModule
import org.seoulsquad.di.repositoryModule
import org.seoulsquad.di.useCaseModule

fun main() {
    startKoin {
        modules(repositoryModule,useCaseModule,csvModule)
    }

}

