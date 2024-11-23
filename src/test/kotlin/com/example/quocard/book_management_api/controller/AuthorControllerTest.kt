package com.example.quocard.book_management_api.controller

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.service.AuthorService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate

@WebMvcTest(controllers = [AuthorController::class])
class AuthorControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var authorService: AuthorService

    @Test
    fun `著者を正常に作成できること`() {
        val author = Author(name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        given(authorService.saveAuthor(any())).willReturn(1L)

        mockMvc.perform(post("/authors")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(author)))
            .andExpect(status().isOk)
            .andExpect(content().string("1"))

        verify(authorService).saveAuthor(author)
    }

    @Test
    fun `指定したIDの著者を取得できること`() {
        val author = Author(id = 1L, name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        given(authorService.findAuthorById(any())).willReturn(author)

        mockMvc.perform(get("/authors/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Author"))
            .andExpect(jsonPath("$.birthDate").value("1990-01-01"))

        verify(authorService).findAuthorById(1L)
    }
}