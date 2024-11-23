package com.example.quocard.book_management_api.service

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.repository.AuthorRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AuthorServiceImpl(private val authorRepository: AuthorRepository) : AuthorService {
    override fun saveAuthor(author: Author): Long {
        if (author.birthDate.isAfter(LocalDate.now())) {
            throw IllegalArgumentException("著者の生年月日は現在の日付よりも過去である必要があります")
        }
        return authorRepository.save(author)
    }

    override fun findAuthorById(id: Long): Author {
        return authorRepository.findById(id)
            ?: throw IllegalArgumentException("指定された著者が存在しません: $id")
    }
}
