package org.seoulsquad.di

import data.utils.CsvLineParser
import data.utils.MealCsvFileReader
import data.utils.MealCsvParser
import org.koin.dsl.module
import org.seoulsquad.data.utils.Constants.CSV_FILE_PATH
import java.io.FileReader

val csvModule= module {
    single { FileReader(CSV_FILE_PATH) }
    single { CsvLineParser() }
    single { MealCsvFileReader(get(),get()) }
    single { MealCsvParser() }
}