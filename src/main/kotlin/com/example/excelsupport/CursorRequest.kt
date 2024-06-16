package com.example.excelsupport

data class CursorRequest(
    val key: Long = -1,
    val chunk: Int,
    val sort: String? = null,
    val direction: String? = null,
    val isLast: Boolean = false,
) {
    val hasKey = key != NONE_KEY

    fun orderBy() = sort
        ?.let { sort ->
            val direction = direction ?: "desc"
            "$sort $direction"
        }
        ?: "id desc"

    fun nextKey(
        key: Long,
    ) =
        if (this.key == key) CursorRequestWrapper(
            copy(isLast = true)
        )
        else CursorRequestWrapper(
            copy(key = key)
        )

    companion object {
        @JvmStatic
        fun of(
            chunk: Int = DEFAULT_CHUNK,
        ) = CursorRequest(chunk = chunk)

        const val NONE_KEY = -1L
        private const val DEFAULT_CHUNK = 100_000
    }
}