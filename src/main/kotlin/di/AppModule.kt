package org.seoulsquad.di

import data.utils.CsvLineParser
import data.utils.MealCsvFileReader
import data.utils.MealCsvParser
import org.koin.dsl.module
import org.seoulsquad.data.repository.MealRepositoryImpl
import org.seoulsquad.data.utils.Constants.CSV_FILE_PATH
import org.seoulsquad.logic.repository.MealRepository
import org.seoulsquad.logic.use_case.GetAllMealsUseCase
import java.io.FileReader

val appModule = module {
    single { FileReader(CSV_FILE_PATH) }
    single { CsvLineParser() }
    single { MealCsvFileReader(get(),get()) }

    single { MealCsvParser() }
    single<MealRepository> { MealRepositoryImpl(get(), get()) }

}