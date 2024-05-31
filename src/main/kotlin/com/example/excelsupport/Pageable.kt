package com.example.excelsupport

data class Pageable(
    val page: Int,
    val size: Int,
    val sort: String,
    val order: String,
)