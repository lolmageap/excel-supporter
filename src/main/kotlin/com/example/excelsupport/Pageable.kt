package com.example.excelsupport

data class Pageable(
    val page: Int,
    val size: Int,
    val sort: String = "id",
    val direction: String = "desc",
) {
    fun toCursorRequest(
        chunk: Int,
    ) =
        CursorRequest(
            size = size,
            sort = sort,
            direction = direction,
            chunk = chunk,
        )
}