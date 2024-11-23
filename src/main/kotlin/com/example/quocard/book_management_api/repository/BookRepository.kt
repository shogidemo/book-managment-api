package com.example.quocard.book_management_api.repository

import com.example.quocard.book_management_api.entity.Book

interface BookRepository {
    fun save(book: Book): Long
    fun findById(id: Long): Book?
    fun findByAuthorId(authorId: Long): List<Book>
}
