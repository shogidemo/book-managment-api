package com.example.quocard.book_management_api.entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AuthorTest {

    @Test
    fun `生年月日が未来の場合は例外が発生する`() {
        assertThrows(IllegalArgumentException::class.java) {
            Author(
                id = null,
                name = "山田 太郎",
                birthDate = LocalDate.now().plusDays(1)
            )
        }
    }

    @Test
    fun `生年月日が過去の場合は正常にインスタンスを作成できる`() {
        val author = Author(
            id = null,
            name = "山田 太郎",
            birthDate = LocalDate.of(1980, 1, 1)
        )
        assertNotNull(author)
    }
}
