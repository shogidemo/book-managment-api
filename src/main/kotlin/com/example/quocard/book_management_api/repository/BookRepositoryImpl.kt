package com.example.quocard.book_management_api.repository

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.book_management_api.entity.Book
import com.example.quocard.jooq.generated.Tables.*
import org.jooq.DSLContext

/**
 * `BookRepositoryImpl`は、`BookRepository`インターフェースの実装クラスであり、
 * 書籍に関連するデータベース操作を担当する。
 * 主にjOOQを利用してデータベース操作を行う。
 */
class BookRepositoryImpl(private val dsl: DSLContext) : BookRepository {

    /**
     * 書籍をデータベースに保存または更新する。
     *
     * @param book 保存または更新する書籍の情報
     * @return 保存または更新した書籍のID
     */
    override fun save(book: Book): Long {
        // 書籍が新規の場合、INSERT文を実行し、生成されたIDを取得
        val bookId = if (book.id == null) {
            dsl.insertInto(BOOKS)
                .set(BOOKS.TITLE, book.title)
                .set(BOOKS.PRICE, book.price)
                .set(BOOKS.PUBLISHED, book.published)
                .returning(BOOKS.ID)
                .fetchOne()!!
                .id
        } else {
            // 既存の書籍の場合、UPDATE文を実行
            dsl.update(BOOKS)
                .set(BOOKS.TITLE, book.title)
                .set(BOOKS.PRICE, book.price)
                .set(BOOKS.PUBLISHED, book.published)
                .where(BOOKS.ID.eq(book.id))
                .execute()
            book.id
        }

        // 書籍と著者の関連を一旦削除
        dsl.deleteFrom(BOOKS_AUTHORS).where(BOOKS_AUTHORS.BOOK_ID.eq(bookId)).execute()

        // 書籍と著者の関連を再挿入
        book.authors.forEach { author ->
            dsl.insertInto(BOOKS_AUTHORS)
                .set(BOOKS_AUTHORS.BOOK_ID, bookId)
                .set(BOOKS_AUTHORS.AUTHOR_ID, author.id)
                .execute()
        }

        return bookId
    }

    /**
     * 指定したIDの書籍を取得する。
     *
     * @param id 書籍のID
     * @return 書籍の情報。存在しない場合はnull。
     */
    override fun findById(id: Long): Book? {
        // 書籍情報を取得
        val bookRecord = dsl.selectFrom(BOOKS).where(BOOKS.ID.eq(id)).fetchOne()
            ?: return null

        // 書籍に関連付けられた著者を取得
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

        // 書籍情報を組み立てて返却
        return Book(
            id = bookRecord.id,
            title = bookRecord.title,
            price = bookRecord.price,
            authors = authors,
            _published = bookRecord.published
        )
    }

    /**
     * 指定した著者に関連付けられた書籍を取得する。
     *
     * @param authorId 著者のID
     * @return 著者に紐づく書籍のリスト
     */
    override fun findByAuthorId(authorId: Long): List<Book> {
        // 著者に関連付けられた書籍を取得
        return dsl.select(BOOKS.ID, BOOKS.TITLE, BOOKS.PRICE, BOOKS.PUBLISHED)
            .from(BOOKS)
            .join(BOOKS_AUTHORS).on(BOOKS.ID.eq(BOOKS_AUTHORS.BOOK_ID))
            .where(BOOKS_AUTHORS.AUTHOR_ID.eq(authorId))
            .fetch { record ->
                // 各書籍に紐づく著者を取得
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

                // 書籍情報を組み立てて返却
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
