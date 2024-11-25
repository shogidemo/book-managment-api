package com.example.quocard.book_management_api.service

import com.example.quocard.book_management_api.entity.Book
import com.example.quocard.book_management_api.repository.AuthorRepository
import com.example.quocard.book_management_api.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) : BookService {

    @Transactional
    override fun saveBook(book: Book): Long {
        if (book.price < BigDecimal.ZERO) throw IllegalArgumentException("価格は0以上である必要があります")
        if (book.authors.isEmpty()) throw IllegalArgumentException("最低1人の著者が必要です")

        book.authors.forEach { author ->
            if (!authorRepository.existsById(author.id!!)) {
                throw IllegalArgumentException("指定された著者が存在しません: ${author.id}")
            }
        }

        //出版済みの書籍を未出版に変更しようとしていないかチェックする。
        if(!book.published){
            book.id?.let { existingBookId ->
                val existingBook = bookRepository.findById(existingBookId)
                    ?: throw IllegalArgumentException("指定された書籍 (ID: $existingBookId) は存在しません。")

                if(existingBook.published == true) throw IllegalArgumentException("出版済みの書籍を未出版には変更できません")
            }
        }

        return bookRepository.save(book)
    }

    override fun findBooksByAuthor(authorId: Long): List<Book> {
        return bookRepository.findByAuthorId(authorId)
    }
}

