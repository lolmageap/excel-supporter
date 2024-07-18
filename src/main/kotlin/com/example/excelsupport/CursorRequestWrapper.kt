package com.example.excelsupport

data class CursorRequestWrapper(
    val cursorRequest: CursorRequest,
) {
    val isLast = cursorRequest.isLast
}