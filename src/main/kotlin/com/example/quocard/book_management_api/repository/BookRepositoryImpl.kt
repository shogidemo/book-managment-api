package com.example.quocard.book_management_api.repository

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.entity.Book
import com.example.quocard.jooq.generated.Tables.*
import org.jooq.DSLContext

class BookRepositoryImpl(private val dsl: DSLContext) : BookRepository {

    override fun save(book: Book): Long {
        val bookId = if (book.id == null) {
            dsl.insertInto(BOOKS)
                .set(BOOKS.TITLE, book.title)
                .set(BOOKS.PRICE, book.price)
                .set(BOOKS.PUBLISHED, book.published)
                .returning(BOOKS.ID)
                .fetchOne()!!
                .id
        } else {
            dsl.update(BOOKS)
                .set(BOOKS.TITLE, book.title)
                .set(BOOKS.PRICE, book.price)
                .set(BOOKS.PUBLISHED, book.published)
                .where(BOOKS.ID.eq(book.id))
                .execute()
            book.id
        }

        //TODO:なんでDELETEしているか謎
        dsl.deleteFrom(BOOKS_AUTHORS).where(BOOKS_AUTHORS.BOOK_ID.eq(bookId)).execute()
        book.authors.forEach { author ->
            dsl.insertInto(BOOKS_AUTHORS)
                .set(BOOKS_AUTHORS.BOOK_ID, bookId)
                .set(BOOKS_AUTHORS.AUTHOR_ID, author.id)
                .execute()
        }

        return bookId
    }

    override fun findById(id: Long): Book? {
        val bookRecord = dsl.selectFrom(BOOKS).where(BOOKS.ID.eq(id)).fetchOne()
            ?: return null

        val authors = dsl.select(AUTHORS.ID, AUTHORS.NAME, AUTHORS.BIRTH_DATE)
            .from(AUTHORS)
            .join(BOOKS_AUTHORS).on(AUTHORS.ID.eq(BOOKS_AUTHORS.AUTHOR_ID))
            .where(BOOKS_AUTHORS.BOOK_ID.eq(id))
            .fetch { record ->
                Author(
                    id = record[AUTHORS.ID],
                    name = record[AUTHORS.NAME],
                    birthDate = record[AUTHORS.BIRTH_DATE]
                )
            }

        val book = Book(
            id = bookRecord.id,
            title = bookRecord.title,
            price = bookRecord.price,
            authors = authors
        )
        book.published = bookRecord.published

        return book
    }

    override fun findByAuthorId(authorId: Long): List<Book> {
        return dsl.select(BOOKS.ID, BOOKS.TITLE, BOOKS.PRICE, BOOKS.PUBLISHED)
            .from(BOOKS)
            .join(BOOKS_AUTHORS).on(BOOKS.ID.eq(BOOKS_AUTHORS.BOOK_ID))
            .where(BOOKS_AUTHORS.AUTHOR_ID.eq(authorId))
            .fetch { record ->
                val authors = dsl.select(AUTHORS.ID, AUTHORS.NAME, AUTHORS.BIRTH_DATE)
                    .from(AUTHORS)
                    .join(BOOKS_AUTHORS).on(AUTHORS.ID.eq(BOOKS_AUTHORS.AUTHOR_ID))
                    .where(BOOKS_AUTHORS.BOOK_ID.eq(record[BOOKS.ID]))
                    .fetch { authorRecord ->
                        Author(
                            id = authorRecord[AUTHORS.ID],
                            name = authorRecord[AUTHORS.NAME],
                            birthDate = authorRecord[AUTHORS.BIRTH_DATE]
                        )
                    }

                Book(
                    id = record[BOOKS.ID],
                    title = record[BOOKS.TITLE],
                    price = record[BOOKS.PRICE],
                    _published = record[BOOKS.PUBLISHED],
                    authors = authors
                )
            }
    }
}
