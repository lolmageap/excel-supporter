package com.example.excelsupport

fun Int.chunked(
    size: Int,
): List<Int> {
    require(size > 0) { "Size must be greater than 0" }

    return mutableListOf<Int>().also {
        val arraySize = this / size
        for (i in 1 until arraySize + 1) {
            it.add(size * i)
        }
    }
}