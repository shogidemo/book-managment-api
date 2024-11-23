package com.example.quocard.book_management_api.repository

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.entity.Book
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.time.LocalDate

@JooqTest
@Import(BookRepositoryImpl::class,AuthorRepositoryImpl::class)
class BookRepositoryTest {

    private lateinit var bookRepositoryImpl: BookRepositoryImpl

    private lateinit var authorRepositoryImpl: AuthorRepositoryImpl

    @Test
    fun `書籍を正常に保存できること`() {
        val author = Author(name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val authorId = authorRepositoryImpl.save(author)
        val registeredAuthor = Author(id = authorId, name = author.name, birthDate = author.birthDate)
        val book = Book(id = 1L, title = "Test Book", price = BigDecimal(1000), authors = listOf(registeredAuthor), _published = true)

        val bookId = bookRepositoryImpl.save(book)

        val retrievedBook = bookRepositoryImpl.findById(bookId)
        assertNotNull(retrievedBook)
        assertEquals("Test Book", retrievedBook?.title)
    }

    @Test
    fun `指定した著者の書籍を取得できること`() {
        val author = Author(name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val authorId = authorRepositoryImpl.save(author)
        val book = Book(id = 1L, title = "Test Book", price = BigDecimal(1000), authors = listOf(author), _published = true)

        bookRepositoryImpl.save(book)
        val books = bookRepositoryImpl.findByAuthorId(authorId)

        assertEquals(1, books.size)
        assertEquals("Test Book", books[0].title)
    }
}
