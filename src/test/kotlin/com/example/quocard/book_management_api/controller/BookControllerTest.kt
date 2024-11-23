package com.example.quocard.book_management_api.controller

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.entity.Book
import com.example.quocard.book_management_api.service.BookService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.mock.mockito.MockBean
import java.math.BigDecimal
import java.time.LocalDate

@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var bookService: BookService

    @Test
    fun `書籍を正常に作成できること`() {
        val author = Author(id = 1L, name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val book = Book(id = 1L, title = "Test Book", price = BigDecimal(1000), authors = listOf(author), _published = true)
        given(bookService.saveBook(any())).willReturn(1L)

        mockMvc.perform(post("/books")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(book)))
            .andExpect(status().isOk)
            .andExpect(content().string("1"))

        verify(bookService).saveBook(book)
    }

    @Test
    fun `指定した著者の書籍を取得できること`() {
        val author = Author(id = 1L, name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val books = listOf(Book(id = 1L, title = "Test Book", price = BigDecimal(1000), authors = listOf(author), _published = true))
        given(bookService.findBooksByAuthor(1L)).willReturn(books)

        mockMvc.perform(get("/books/author/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].title").value("Test Book"))
            .andExpect(jsonPath("$[0].authors[0].name").value("Test Author"))

        verify(bookService).findBooksByAuthor(1L)
    }
}
