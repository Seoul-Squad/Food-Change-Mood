package org.seoulsquad.presentation.utils

import org.seoulsquad.presentation.consolelIO.Reader
import org.seoulsquad.presentation.consolelIO.Viewer

class TablePrinter(
    private val viewer: Viewer, private val reader: Reader
) {
    fun printTable(
        headers: List<String>, columnValues: List<List<String>>
    ) {
        if (columnValues.size != headers.size) throw IllegalArgumentException("Headers count should be same as columns count")
        val columnWidths = calculateColumnsWidth(headers, columnValues)
        val paginatedValues = columnValues.map { it.chunked(TABLE_PAGE_SIZE) }
        paginatedValues.forEachIndexed { index, valuesPage ->
            printTablePage(
                pagesCount = paginatedValues.size,
                pageIndex = index,
                columnValues = valuesPage,
                headers = headers,
                columnWidths = columnWidths
            )
            if (doesUserWantToExit(index, paginatedValues.size)) return
        }
    }

    private fun printTablePage(
        columnWidths: List<Int>,
        headers: List<String>,
        columnValues: List<List<String>>,
        pagesCount: Int,
        pageIndex: Int
    ) {
        printSeparatorLine(columnWidths)
        printHeaderLine(columnWidths, headers)
        printSeparatorLine(columnWidths)
        val rowsCount = headers.maxOf { it.length }
        for (rowIndex in 0 until rowsCount) {
            val rowValues = columnValues.map { column ->
                column.getOrElse(rowIndex) { "" }
            }
            printRowLine(columnWidths, rowValues)
        }
        printSeparatorLine(columnWidths)
        viewer.display("=== Page ${pageIndex + 1} of $pagesCount ===")
    }


    private fun printSeparatorLine(
        columnWidths: List<Int>
    ) {
        val separatorLine = columnWidths.joinToString("") { width ->
            "|-" + "-".repeat(width) + "-"
        } + "|"

        viewer.display(separatorLine)
    }

    private fun printHeaderLine(columnWidths: List<Int>, headers: List<String>) {
        val headerLine = columnWidths.zip(headers).joinToString("") { (width, header) ->
            "| " + header.padEnd(width) + " "
        } + "|"

        viewer.display(headerLine)
    }

    private fun printRowLine(columnWidths: List<Int>, rowValues: List<String>) {
        val rowLine = columnWidths.zip(rowValues).joinToString("") { (width, value) ->
            "| " + value.padEnd(width) + " "
        } + "|"

        viewer.display(rowLine)
    }

    private fun calculateColumnsWidth(headers: List<String>, columnValues: List<List<String>>): List<Int> {
        return headers.zip(columnValues).map { (header, values) ->
            maxOf(header.length, values.maxOfOrNull { it.length } ?: 0)
        }
    }

    private fun doesUserWantToExit(
        index: Int,
        pagesCount: Int,
    ): Boolean {
        if (index != pagesCount - 1) {
            while (true) {
                viewer.display("Press Enter to view next page or 0 to exit.")
                when (reader.readString().trim()) {
                    "" -> break
                    "0" -> return true
                    else -> viewer.display("Invalid input, Please retry again")
                }
            }
        }
        return false
    }

    companion object {
        const val TABLE_PAGE_SIZE = 1000
    }
}
