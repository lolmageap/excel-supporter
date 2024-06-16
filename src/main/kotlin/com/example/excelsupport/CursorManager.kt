package com.example.excelsupport

object CursorManager {
    tailrec fun loop(
        cursorRequest: CursorRequest,
        block: (CursorRequest) -> CursorRequestWrapper,
    ) {
        val response = block.invoke(cursorRequest)
        if (response.cursorRequest.isLast) return
        else loop(response.cursorRequest, block)
    }
}