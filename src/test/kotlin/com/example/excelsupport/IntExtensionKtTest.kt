package com.example.excelsupport

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IntExtensionKtTest {
    @Test
    @DisplayName("100,000을 2,000으로 나누어 50개의 청크 리스트로 반환하는 기능")
    fun chunked() {
        val chunked = 100_000.chunked(2_000)

        assert(chunked.size == 50)
        assert(chunked[0] == 2_000)
        assert(chunked[49] == 100_000)
    }
}