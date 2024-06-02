package com.example.excelsupport

data class CursorRequest(
    var key: Int = -1,
    val size: Int,
    val chunk: Int,
    val sort: String?,
    val direction: String?,
) {
    // 현재 data class에 너무 많은 책임이 있습니다. 어떻게 분리를 해야할지 고민이 필요합니다.
    fun loop(
        block: (CursorRequest) -> Unit,
    ) {
        val index = if (size % chunk == 0) size / chunk
        else (size / chunk) + 1

        for (i in 0 until index) {
            if (i.isLast) {
                limit = chunk * i - size
                block(this)
            } else {
                block(this)
            }
        }
    }

    var limit = chunk

    private val Int.isLast
        get() = size <= chunk * this

    val hasKey = key != NONE_KEY

    fun orderBy() = sort
        ?.let { sort ->
            val direction = direction ?: "desc"
            "$sort $direction"
        }
        ?: "id desc"

    companion object {
        const val NONE_KEY = -1
    }
}