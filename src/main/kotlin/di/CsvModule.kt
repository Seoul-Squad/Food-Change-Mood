package di

import data.utils.Constants.CSV_FILE_PATH
import data.utils.CsvFileReader
import data.utils.CsvLineParser
import data.utils.MealCsvParser
import org.koin.dsl.module
import java.io.FileReader

val csvModule =
    module {
        single { FileReader(CSV_FILE_PATH) }
        single { CsvLineParser() }
        single { CsvFileReader(get(), get()) }
        single { MealCsvParser() }
    }
