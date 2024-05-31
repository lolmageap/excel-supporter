package com.example.excelsupport

import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.io.File

@SpringBootTest
class ExcelSupportTests(
    @Autowired private val excelManager: ExcelManager,
    @Autowired private val httpServletResponse: HttpServletResponse,
) {
    @Test
    @DisplayName("엑셀 파일 생성")
    fun createFile() {
        val file = excelManager.createFile(Person::class)
        file.delete()
    }

    @Test
    @DisplayName("매개 변수가 data class 가 아닌 경우 예외 발생")
    fun failTest() {
        assertThrows<IllegalArgumentException> { excelManager.createFile(Dog::class) }
    }

    @Test
    @DisplayName("엑셀 파일에 데이터 추가 테스트")
    fun writeBody() {
        val person = Person("John", 30, 180)
        val person2 = Person("Jane", 25, 170)
        val person3 = Person("Tom", 27, 175)
        val people = listOf(person, person2, person3)

        val file = excelManager.createFile(Person::class)
        excelManager.writeBody(file, people)
        file.delete()
    }

    @Test
    @DisplayName("엑셀 파일이 아닌 경우 예외 발생")
    fun failWriteBody() {
        val person = Person("John", 30, 180)
        val person2 = Person("Jane", 25, 170)
        val person3 = Person("Tom", 27, 175)
        val people = listOf(person, person2, person3)

        val txtFile = File("test.txt")
        assertThrows<IllegalArgumentException> { excelManager.writeBody(txtFile, people) }
        txtFile.delete()
    }

    @Test
    @DisplayName("파일 다운로드")
    fun downloadFile() {
        val person = Person("John", 30, 180)
        val person2 = Person("Jane", 25, 170)
        val person3 = Person("Tom", 27, 175)
        val people = listOf(person, person2, person3)

        val file = excelManager.createFile(Person::class)
        excelManager.downloadFile(file, httpServletResponse)
    }

    @Test
    @DisplayName("엑셀 파일을 생성 후 대용량의 데이터를 추가하고 다운로드")
    fun createAndDownloadFile() {
        val people = (1..1_000_000).map { Person("John", 30, 180) }

        val pageable = Pageable(1, 100_000, "name", "asc")
        val chunked = pageable.size.chunked(2000)
        val file = excelManager.createFile(Person::class)

        repeat(chunked.size) {
            val start = it * 2000
            val end = if (it == chunked.size - 1) pageable.size else (it + 1) * 2000
            val subList = people.subList(start, end)
            excelManager.writeBody(file, subList)
        }

        excelManager.downloadFile(file, httpServletResponse)
    }
}
