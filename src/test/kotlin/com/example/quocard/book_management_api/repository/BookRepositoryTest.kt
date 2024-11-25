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
    fun `登録済みのデータを更新できること`() {
        // 既存データの作成
        val author = Author(name = "Author", birthDate = LocalDate.of(1980, 1, 1))
        val authorId = authorRepository.save(author)

        val initialBook = Book(
            title = "Initial Book",
            price = BigDecimal("1000"),
            _published = false,
            authors = listOf(Author(id = authorId, name = author.name, birthDate = author.birthDate))
        )
        val bookId = bookRepository.save(initialBook)

        // 更新データの準備
        val updatedBook = Book(
            id = bookId,
            title = "Updated Book",
            price = BigDecimal("2000"),
            _published = true,
            authors = listOf(Author(id = authorId, name = author.name, birthDate = author.birthDate))
        )
        val updatedId = bookRepository.save(updatedBook)

        // データベースから取得
        val result = bookRepository.findById(updatedId)

        // 検証
        assertNotNull(result)
        assertEquals("Updated Book", result?.title)
        assertEquals(BigDecimal("2000.00"), result?.price)
        assertTrue(result?.published ?: false)
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
