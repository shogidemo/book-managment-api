package com.example.quocard.book_management_api.service

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.repository.AuthorRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDate

class AuthorServiceTest {
    private val authorRepository = mock(AuthorRepository::class.java)
    private val authorService = AuthorServiceImpl(authorRepository)

    @Test
    fun `著者を正常に登録できること`() {
        val author = Author(name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        `when`(authorRepository.save(author)).thenReturn(1L)

        val authorId = authorService.saveAuthor(author)

        assertEquals(1L, authorId)
        verify(authorRepository).save(author)
    }

    @Test
    fun `存在しないIDを指定した場合、例外が発生すること`() {
        `when`(authorRepository.findById(99L)).thenReturn(null)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            authorService.findAuthorById(99L)
        }

        assertEquals("指定された著者が存在しません: 99", exception.message)
    }
}
