package com.example.excelsupport

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IntExtensionKtTest {
    @Test
    @DisplayName("Pageable 객체의 크기 100,000을 주어진 크기 2,000으로 나누어 50개의 청크 리스트로 반환하는 기능")
    fun chunked() {
        val pageable = Pageable(1, 100_000, "name", "asc")
        val chunked = pageable.size.chunked(2_000)

        assert(chunked.size == 50)
        assert(chunked[0] == 2_000)
        assert(chunked[49] == 100_000)
    }
}