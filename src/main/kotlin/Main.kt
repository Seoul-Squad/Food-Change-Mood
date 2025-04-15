package org.seoulsquad

import data.model.CsvData
import org.koin.core.context.startKoin
import org.seoulsquad.di.csvModule
import org.seoulsquad.di.repositoryModule
import org.seoulsquad.di.useCaseModule
import org.seoulsquad.presentation.ConsoleUi


fun main() {
    startKoin {
        modules(repositoryModule,useCaseModule,csvModule)
    }

}

