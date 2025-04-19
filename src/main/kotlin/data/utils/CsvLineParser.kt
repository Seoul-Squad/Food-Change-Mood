package data.utils

class CsvLineParser {

    fun parseCsvLine(line: String): List<String> {
        val fields = mutableListOf<String>()
        val currentField = StringBuilder()
        var insideQuotes = false

        for (char in line) {
            when {
                char == '"' -> insideQuotes = !insideQuotes
                char == ',' && !insideQuotes -> {
                    fields.add(currentField.toString().trim())
                    currentField.clear()
                }
                else -> currentField.append(char)
            }
        }

        fields.add(currentField.toString().trim())

        return fields
    }
}