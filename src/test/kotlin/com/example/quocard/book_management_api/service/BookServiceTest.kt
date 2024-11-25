package com.example.quocard.book_management_api.service

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.entity.Book
import com.example.quocard.book_management_api.repository.AuthorRepository
import com.example.quocard.book_management_api.repository.BookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.time.LocalDate

class BookServiceTest {
    private val bookRepository = mock(BookRepository::class.java)
    private val authorRepository = mock(AuthorRepository::class.java)
    private val bookService = BookServiceImpl(bookRepository, authorRepository)

    @Test
    fun `本を正常に登録できること`() {
        val author = Author(id = 1L, name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val book = Book(id = 1L, title = "Test Book", price = BigDecimal(1000), authors = listOf(author), _published = true)

        `when`(authorRepository.existsById(1L)).thenReturn(true)
        `when`(bookRepository.save(book)).thenReturn(1L)

        val bookId = bookService.saveBook(book)

        assertEquals(1L, bookId)
        verify(bookRepository).save(book)
    }

    @Test
    fun `著者が存在しない場合、例外が発生すること`() {
        val author = Author(id = 99L, name = "Unknown Author", birthDate = LocalDate.of(1990, 1, 1))
        val book = Book(id = 1L,title = "Test Book", price = BigDecimal(1000), authors = listOf(author), _published = true)

        `when`(authorRepository.existsById(99L)).thenReturn(false)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            bookService.saveBook(book)
        }

        assertEquals("指定された著者が存在しません: 99", exception.message)
    }

    @Test
    fun `出版済みの書籍を未出版に変更しようとした場合、例外が発生すること`() {
        val author = Author(id = 1L, name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val existingBook = Book(id = 1L, title = "Existing Book", price = BigDecimal(1000), authors = listOf(author), _published = true)
        val updatedBook = Book(id = 1L, title = "Updated Book", price = BigDecimal(1000), authors = listOf(author), _published = false)

        `when`(bookRepository.findById(1L)).thenReturn(existingBook)
        `when`(authorRepository.existsById(1L)).thenReturn(true)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            bookService.saveBook(updatedBook)
        }

        assertEquals("出版済みの書籍を未出版には変更できません", exception.message)
    }
}

