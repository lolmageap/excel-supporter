package com.example.excelsupport

object CursorManager {
    tailrec fun <T> loop(
        cursorRequest: CursorRequest,
        block: (CursorRequest) -> Collection<T>,
    ) {
        val response = block.invoke(cursorRequest)
        if (response.isNotEmpty()) {
            loop(cursorRequest, block)
        }
    }
}