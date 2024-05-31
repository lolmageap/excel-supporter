package com.example.excelsupport

import jakarta.servlet.http.HttpServletResponse
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.*
import kotlin.reflect.full.memberProperties

private typealias DataClass = Any

@Component
class ExcelManager {
    fun createFile(
        dataClasses: List<DataClass>,
    ): File {
        validate(dataClasses)
        val header = writeHeader(dataClasses)
        val filename = UUID.randomUUID().toString()
        return makeFile(header, filename)
    }

    fun writeBody(
        file: File,
        dataClasses: List<DataClass>,
    ) {
        file.extension.takeIf { it == "xlsx" }
            ?: throw IllegalArgumentException("Only xlsx files are allowed")

        file.inputStream().use { inputStream ->
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)

            dataClasses.forEach { dataClass ->
                val row = sheet.createRow(sheet.lastRowNum + 1)

                dataClass.javaClass.declaredFields.mapNotNull { field ->
                    if (field.name == "Companion") null
                    else {
                        field.isAccessible = true
                        field.get(dataClass)
                    }
                }.forEachIndexed { index, value ->
                    val cell = row.createCell(index)
                    cell.setCellValue(value.toString())
                }
            }

            FileOutputStream(file).use { outputStream ->
                workbook.write(outputStream)
            }

            workbook.close()
        }
    }

    fun downloadFile(
        file: File,
        httpServletResponse: HttpServletResponse,
    ) {
        file.extension.takeIf { it == "xlsx" }
            ?: throw IllegalArgumentException("Only xlsx files are allowed")

        httpServletResponse.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
        val encodedFilename = URLEncoder.encode(file.name, "UTF-8").replace("+", "%20")
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''$encodedFilename")
        httpServletResponse.outputStream.write(file.readBytes())
        file.delete()
    }

    private fun makeFile(
        header: List<String>,
        filename: String,
    ): File {
        val workbook = XSSFWorkbook()
        workbook.createSheet("Sheet1")
        val firstRow = workbook.getSheetAt(0).createRow(0)

        header.forEachIndexed { index, value ->
            firstRow.createCell(index).setCellValue(value)
        }

        val file = File("$filename.xlsx")

        FileOutputStream(file).use { fileOut ->
            workbook.write(fileOut)
        }

        workbook.close()
        return file
    }

    private fun writeHeader(
        dataClasses: List<DataClass>,
    ) = dataClasses.first().javaClass.kotlin.memberProperties.map {
        it.annotations.filterIsInstance<ExcelHeader>().firstOrNull()?.name
            ?: it.name
    }

    private fun validate(
        dataClass: List<DataClass>,
    ) {
        val isDataClass = dataClass.first().javaClass.kotlin.isData
        require(isDataClass) { "Only data classes are allowed" }
        require(dataClass.isNotEmpty()) { "Data class list is empty" }
    }
}