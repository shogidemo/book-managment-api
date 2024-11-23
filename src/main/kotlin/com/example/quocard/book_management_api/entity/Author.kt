package com.example.quocard.book_management_api.entity

import java.time.LocalDate

data class Author(
    val id: Long? = null,
    val name: String,
    val birthDate: LocalDate
) {
    init {
        if (birthDate.isAfter(LocalDate.now())) {
            throw IllegalArgumentException("生年月日は現在の日付よりも過去である必要があります")
        }
    }
}
