package com.example.excelsupport

import org.springframework.stereotype.Component
import kotlin.reflect.full.memberProperties

private typealias DataClass = Any

@Component
class ExcelManager {
    fun readExcel() {}

    fun writeExcel(dataClass: DataClass) {
        val isDataClass = dataClass.javaClass.kotlin.isData
        require(isDataClass) { "Only data classes are allowed" }

        val properties = dataClass.javaClass.kotlin.memberProperties.map {
            it.annotations.filterIsInstance<ExcelHeader>().firstOrNull()?.name
                ?: it.name
        }

        println("Header: $properties")

        val row = dataClass.javaClass.kotlin.memberProperties.map { it.get(dataClass) }
        println("Row: $row")
    }

    fun writeExcel(dataClasses: List<DataClass>) {
        dataClasses.first().let {
            val isDataClass = it.javaClass.kotlin.isData
            require(isDataClass) { "Only data classes are allowed" }
        }

        val properties = dataClasses.first().javaClass.kotlin.memberProperties.map {
            it.annotations.filterIsInstance<ExcelHeader>().firstOrNull()?.name
                ?: it.name
        }

        println("Header: $properties")

        dataClasses.map { dataClass ->
            val row = dataClass.javaClass.kotlin.memberProperties.map { it.get(dataClass) }
            println("Row: $row")
        }
    }
}