package com.example.quocard.book_management_api.repository

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.entity.Book
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.time.LocalDate

@JooqTest
@Import(BookRepositoryImpl::class,AuthorRepositoryImpl::class)
class BookRepositoryTest {

    @Autowired lateinit var bookRepository: BookRepository

    @Autowired lateinit var authorRepository: AuthorRepository

    @Test
    fun `書籍を正常に保存できること`() {
        val author = Author(name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val authorId = authorRepository.save(author)
        val registeredAuthor = Author(id = authorId, name = author.name, birthDate = author.birthDate)
        val book = Book(id = 1L, title = "Test Book", price = BigDecimal(1000), authors = listOf(registeredAuthor), _published = true)

        val bookId = bookRepository.save(book)

        val retrievedBook = bookRepository.findById(bookId)
        assertNotNull(retrievedBook)
        assertEquals("Test Book", retrievedBook?.title)
    }

    @Test
    fun `指定した著者の書籍を取得できること`() {
        val author = Author(name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val authorId = authorRepository.save(author)
        val registeredAuthor = Author(id = authorId, name = author.name, birthDate = author.birthDate)
        val book = Book(id = 1L, title = "Test Book", price = BigDecimal(1000), authors = listOf(registeredAuthor), _published = true)

        bookRepository.save(book)
        val books = bookRepository.findByAuthorId(authorId)

        assertEquals(1, books.size)
        assertEquals("Test Book", books[0].title)
    }
}
