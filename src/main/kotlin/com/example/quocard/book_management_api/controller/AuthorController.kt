package com.example.quocard.book_management_api.controller

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.service.AuthorService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/authors")
class AuthorController(private val authorService: AuthorService) {

    @PostMapping
    fun saveAuthor(@RequestBody author: Author): ResponseEntity<Long> {
        val authorId = authorService.saveAuthor(author)
        return ResponseEntity.ok(authorId)
    }

    @GetMapping("/{id}")
    fun getAuthor(@PathVariable id: Long): ResponseEntity<Author> {
        val author = authorService.findAuthorById(id)
        return ResponseEntity.ok(author)
    }
}
