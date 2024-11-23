package com.example.quocard.book_management_api.service

import com.example.quocard.book_management_api.entity.Author

interface AuthorService {
    fun saveAuthor(author: Author): Long
    fun findAuthorById(id: Long): Author
}
