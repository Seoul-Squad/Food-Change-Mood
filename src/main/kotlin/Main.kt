package org.seoulsquad

import data.model.CsvData
import data.utils.MealCsvFileReader
import data.utils.MealCsvParser
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin
import org.seoulsquad.di.csvModule
import org.seoulsquad.di.repositoryModule
import org.seoulsquad.di.useCaseModule
import org.seoulsquad.presentation.ConsoleUi
import java.io.File
import java.io.FileReader


fun main() {
    startKoin {
        modules(repositoryModule,useCaseModule,csvModule)
    }

    val consoleUi : ConsoleUi = getKoin().get()
    consoleUi.startKetoDietFlow()
}

