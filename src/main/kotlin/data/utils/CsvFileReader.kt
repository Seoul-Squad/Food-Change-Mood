package data.utils

import data.model.CsvData
import java.io.BufferedReader
import java.io.FileReader

class CsvFileReader(
    private val fileReader: FileReader,
    private val parser: CsvLineParser
) {
    fun readCsv(): CsvData = BufferedReader(fileReader).use { reader ->
        reader.lineSequence()
            .map { parser.parseCsvLine(it) }
            .filter { it.isNotEmpty() }
            .toList()
            .let { lines ->
                when {
                    lines.isEmpty() -> CsvData(emptyList(), emptyList())
                    else -> CsvData(
                        headers = lines.first(),
                        rows = lines.drop(1).filter { it.size >= lines.first().size }
                    )
                }
            }
    }
}