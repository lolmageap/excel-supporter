package com.example.excelsupport

data class CursorRequest(
    var key: Int = -1,
    val chunk: Int,
    val sort: String? = null,
    val direction: String? = null,
) {
    val hasKey = key != NONE_KEY

    fun orderBy() = sort
        ?.let { sort ->
            val direction = direction ?: "desc"
            "$sort $direction"
        }
        ?: "id desc"

    companion object {
        @JvmStatic
        fun of(chunk: Int = DEFAULT_CHUNK) =
            CursorRequest(chunk = chunk)

        const val NONE_KEY = -1
        private const val DEFAULT_CHUNK = 2_000
    }
}