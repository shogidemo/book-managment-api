package com.example.quocard.book_management_api.repository

import com.example.quocard.book_management_api.entity.Author
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import java.time.LocalDate

@JooqTest
@Import(AuthorRepositoryImpl::class)
class AuthorRepositoryTest {

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Test
    fun `著者を正常に保存できること`() {
        val author = Author(name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val authorId = authorRepository.save(author)

        val retrievedAuthor = authorRepository.findById(authorId)
        assertNotNull(retrievedAuthor)
        assertEquals("Test Author", retrievedAuthor?.name)
    }

    @Test
    fun `登録済みのデータを更新できること`() {
        // 既存データの作成
        val initialAuthor = Author(name = "Initial Author", birthDate = LocalDate.of(1980, 1, 1))
        val authorId = authorRepository.save(initialAuthor)

        // 更新データの準備
        val updatedAuthor = Author(id = authorId, name = "Updated Author", birthDate = LocalDate.of(1980, 1, 2))
        val updatedId = authorRepository.save(updatedAuthor)

        // データベースから取得
        val result = authorRepository.findById(updatedId)

        // 検証
        assertNotNull(result)
        assertEquals("Updated Author", result?.name)
        assertEquals(LocalDate.of(1980, 1, 2), result?.birthDate)
    }

    @Test
    fun `指定したIDの著者が存在するか確認できること`() {
        val author = Author(name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val authorId = authorRepository.save(author)

        assertTrue(authorRepository.existsById(authorId))
    }
}

