package com.example.quocard.book_management_api.controller

import com.example.quocard.book_management_api.entity.Book
import com.example.quocard.book_management_api.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService) {

    @PostMapping
    fun saveBook(@RequestBody book: Book): ResponseEntity<Long> {
        val bookId = bookService.saveBook(book)
        return ResponseEntity.ok(bookId)
    }

    @GetMapping("/author/{authorId}")
    fun getBooksByAuthor(@PathVariable authorId: Long): ResponseEntity<List<Book>> {
        val books = bookService.findBooksByAuthor(authorId)
        return ResponseEntity.ok(books)
    }
}


