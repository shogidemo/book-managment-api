package com.example.quocard.book_management_api.repository

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.jooq.generated.Tables.AUTHORS
import org.jooq.DSLContext

class AuthorRepositoryImpl(private val dsl: DSLContext) : AuthorRepository {
    override fun save(author: Author): Long {
        return if (author.id == null) {
            dsl.insertInto(AUTHORS)
                .set(AUTHORS.NAME, author.name)
                .set(AUTHORS.BIRTH_DATE, author.birthDate)
                .returning(AUTHORS.ID)
                .fetchOne()!!
                .id
        } else {
            dsl.update(AUTHORS)
                .set(AUTHORS.NAME, author.name)
                .set(AUTHORS.BIRTH_DATE, author.birthDate)
                .where(AUTHORS.ID.eq(author.id))
                .execute()
            author.id
        }
    }

    override fun findById(id: Long): Author? {
        return dsl.selectFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id))
            .fetchOne { record ->
                Author(
                    id = record[AUTHORS.ID],
                    name = record[AUTHORS.NAME],
                    birthDate = record[AUTHORS.BIRTH_DATE]
                )
            }
    }

    override fun existsById(id: Long): Boolean {
        return dsl.fetchExists(dsl.selectFrom(AUTHORS).where(AUTHORS.ID.eq(id)))
    }
}
