package com.example.excelsupport

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
class ExcelSupportMockTests {
    @MockkBean private lateinit var excelManager: ExcelManager
    @Autowired private lateinit var httpServletResponse: HttpServletResponse

    @Test
    @DisplayName("엑셀 파일을 생성 후 2000개 이하의 데이터를 추가하고 다운로드")
    fun createAndDownloadFile() {
        val people = (1..1_000).map { Person("John", 30, 180) }

        every { excelManager.createFile(Person::class) } returns File("test.xlsx")
        every { excelManager.writeBody(any(), people) } returns Unit
        every { excelManager.downloadFile(any(), httpServletResponse) } returns Unit

        val file = excelManager.createFile(Person::class)
        excelManager.writeBody(file, people)
        excelManager.downloadFile(file, httpServletResponse)

        verify { excelManager.createFile(Person::class) }
        verify { excelManager.writeBody(file, people) }
        verify { excelManager.downloadFile(file, any()) }
    }

    @Test
    @DisplayName("엑셀 파일을 생성 후 2000개 이상의 데이터를 추가하고 다운로드")
    fun bulkDownload() {
        val people = (1..10_000).map { Person("John", 30, 180, it) }
        val cursorRequest = CursorRequest.of(2000)

        every { excelManager.createFile(Person::class) } returns File("test.xlsx")
        every { excelManager.writeBody(any(), any()) } returns Unit
        every { excelManager.downloadFile(any(), any()) } returns Unit

        val file = excelManager.createFile(Person::class)
        CursorManager.loop(cursorRequest) { cursor ->
            people.findAllById(cursor.key, cursor.chunk)
                .also { findPeople ->
                    excelManager.writeBody(file, findPeople)
                    cursor.key = findPeople.last().id
                }
        }
        excelManager.downloadFile(file, httpServletResponse)

        verify { excelManager.createFile(Person::class) }
        verify(exactly = 5) { excelManager.writeBody(file, any()) }
        verify { excelManager.downloadFile(file, any()) }
    }

    @Test
    @DisplayName("엑셀 파일을 생성 후 2000개 이상의 데이터를 추가하고 다운로드 경계값 테스트")
    fun bulkDownloadV2() {
        val people = (1..10_001).map { Person("John", 30, 180, it) }
        val cursorRequest = CursorRequest.of(2000)

        every { excelManager.createFile(Person::class) } returns File("test.xlsx")
        every { excelManager.writeBody(any(), any()) } returns Unit
        every { excelManager.downloadFile(any(), any()) } returns Unit

        val file = excelManager.createFile(Person::class)
        CursorManager.loop(cursorRequest) { cursor ->
            people.findAllById(cursor.key, cursor.chunk)
                .also { findPeople ->
                    excelManager.writeBody(file, findPeople)
                    cursor.key = findPeople.last().id
                }
        }
        excelManager.downloadFile(file, httpServletResponse)

        verify { excelManager.createFile(Person::class) }
        verify(exactly = 6) { excelManager.writeBody(file, any()) }
        verify { excelManager.downloadFile(file, any()) }
    }
}

private fun List<Person>.findAllById(
    id: Int,
    chunk: Int = 2000,
) = this.filter { it.id > id }.take(chunk)