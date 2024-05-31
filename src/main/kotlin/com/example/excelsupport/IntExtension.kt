package com.example.excelsupport

fun Int.chunked(size: Int): List<Int> {
    val list = mutableListOf<Int>()
    var num = this
    while (num > 0) {
        list.add(num % size)
        num /= size
    }
    return list.reversed()
}