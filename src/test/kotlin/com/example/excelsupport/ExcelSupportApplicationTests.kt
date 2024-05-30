package com.example.excelsupport

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ExcelSupportApplicationTests(
    @Autowired private val excelManager: ExcelManager,
) {
    @Test
    @DisplayName("단일 데이터 엑셀 파일 작성 테스트")
    fun successTest() {
        val person = Person("John", 30, 180)
        excelManager.writeExcel(person)
    }

    @Test
    @DisplayName("대용량 데이터 엑셀 파일 작성 테스트")
    fun contextLoads() {
        val person = Person("John", 30, 180)
        val person2 = Person("Jane", 25, 170)
        val person3 = Person("Tom", 27, 175)
        val people = listOf(person, person2, person3)

        excelManager.writeExcel(people)
    }

    @Test
    @DisplayName("data class 가 아닌 경우 예외 발생 테스트")
    fun failTest() {
        val dog = Dog("회운", 3)
        assertThrows<IllegalArgumentException> { excelManager.writeExcel(dog) }
    }
}
