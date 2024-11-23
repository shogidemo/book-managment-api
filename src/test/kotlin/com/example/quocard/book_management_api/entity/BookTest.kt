package com.example.quocard.book_management_api.entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class BookTest {

    @Test
    fun `価格が0以上であるとき、正常に作成できること`() {
        val authors = listOf(Author(id = 1, name = "Author1", birthDate = LocalDate.of(1980, 1, 1)))
        val book = Book(id = 1, title = "Test Book", price = BigDecimal("1000.50"), authors = authors)

        assertEquals("Test Book", book.title)
        assertEquals(BigDecimal("1000.50"), book.price)
        assertEquals(authors, book.authors)
        assertFalse(book.published)
    }

    @Test
    fun `価格が0未満のとき、例外が発生すること`() {
        val authors = listOf(Author(id = 1, name = "Author1", birthDate = LocalDate.of(1980, 1, 1)))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            Book(id = 1, title = "Test Book", price = BigDecimal("-1.00"), authors = authors)
        }

        assertEquals("価格は0以上である必要があります", exception.message)
    }

    @Test
    fun `著者が0人のとき、例外が発生すること`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Book(id = 1, title = "Test Book", price = BigDecimal("1000.00"), authors = emptyList())
        }

        assertEquals("書籍は最低1人の著者を持つ必要があります", exception.message)
    }

    @Test
    fun `出版済みの書籍の出版状況を未出版に変更すると例外が発生すること`() {
        val authors = listOf(Author(id = 1, name = "Author1", birthDate = LocalDate.of(1980, 1, 1)))
        val book = Book(id = 1, title = "Test Book", price = BigDecimal("1000.00"), authors = authors)

        book.published = true // 出版済みに設定

        val exception = assertThrows(IllegalArgumentException::class.java) {
            book.published = false // 未出版に戻そうとする
        }

        assertEquals("出版済みの書籍の出版状況を未出版に変更することはできません", exception.message)
    }
}
