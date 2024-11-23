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
    fun `指定したIDの著者が存在するか確認できること`() {
        val author = Author(name = "Test Author", birthDate = LocalDate.of(1990, 1, 1))
        val authorId = authorRepository.save(author)

        assertTrue(authorRepository.existsById(authorId))
    }
}

