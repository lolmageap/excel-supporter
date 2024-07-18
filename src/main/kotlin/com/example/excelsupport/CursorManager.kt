package com.example.excelsupport

object CursorManager {
    tailrec fun loop(
        cursorRequest: CursorRequest = CursorRequest.of(),
        block: (CursorRequest) -> CursorRequestWrapper,
    ) {
        val response = block.invoke(cursorRequest)
        if (response.isLast) return
        else loop(response.cursorRequest, block)
    }
}