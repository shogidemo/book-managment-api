package com.example.quocard.book_management_api.repository

import com.example.quocard.book_management_api.entity.Author

interface AuthorRepository {
    fun save(author: Author): Long
    fun findById(id: Long): Author?
    fun existsById(id: Long): Boolean
}
