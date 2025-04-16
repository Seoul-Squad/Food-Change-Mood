package data.utils

import data.model.CsvData
import java.io.BufferedReader
import java.io.FileReader

class CsvFileReader(private val fileReader: FileReader, private val parser: CsvLineParser) {

    fun readCsv(): CsvData {
        val rows = mutableListOf<List<String>>()
        var headers = emptyList<String>()
        var isFirstLine = true

        BufferedReader(fileReader).use { reader ->
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                line?.let {
                    val parsed = parser.parseCsvLine(it)
                    if (isFirstLine) {
                        headers = parsed
                        isFirstLine = false
                    } else {
                        if (parsed.size >= headers.size) {
                            rows.add(parsed)
                        }
                    }
                }
            }
        }
        return CsvData(headers, rows)
    }
}