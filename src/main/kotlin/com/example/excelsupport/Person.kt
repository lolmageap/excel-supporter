package com.example.excelsupport

data class Person(
    @ExcelHeader("이름")
    val name: String,

    @ExcelHeader("나이")
    val age: Int,

    val height: Int,

    val id: Int = 0,
)