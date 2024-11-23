package com.example.quocard.book_management_api.service

import com.example.quocard.book_management_api.entity.Book
import org.springframework.transaction.annotation.Transactional

interface BookService {
    fun saveBook(book: Book): Long
    fun findBooksByAuthor(authorId: Long): List<Book>
}
