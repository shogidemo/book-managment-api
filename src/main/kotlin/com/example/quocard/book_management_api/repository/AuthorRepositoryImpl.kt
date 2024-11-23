package com.example.quocard.book_management_api.repository

import com.example.quocard.book_management_api.entity.Author
import com.example.quocard.jooq.generated.Tables.AUTHORS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

/**
 * AuthorRepositoryImpl は Author エンティティを操作するためのリポジトリ実装クラス。
 * jOOQ を使用してデータベース操作を実現する。
 *
 * @param dsl jOOQ の DSLContext インスタンスを使用してクエリを実行する。
 */
@Repository
class AuthorRepositoryImpl(private val dsl: DSLContext) : AuthorRepository {

    /**
     * 著者を保存または更新する。
     * - 著者の ID が null の場合は新規登録を行い、生成された ID を返す。
     * - ID が存在する場合は既存のレコードを更新する。
     *
     * @param author 保存または更新する Author エンティティ。
     * @return 保存または更新された著者の ID。
     */
    override fun save(author: Author): Long {
        return if (author.id == null) {
            // 新規登録
            dsl.insertInto(AUTHORS)
                .set(AUTHORS.NAME, author.name)
                .set(AUTHORS.BIRTH_DATE, author.birthDate)
                .returning(AUTHORS.ID) // 挿入後に生成された ID を取得
                .fetchOne()!! // null の可能性は排除
                .id
        } else {
            // 既存レコードの更新
            dsl.update(AUTHORS)
                .set(AUTHORS.NAME, author.name)
                .set(AUTHORS.BIRTH_DATE, author.birthDate)
                .where(AUTHORS.ID.eq(author.id)) // ID でレコードを特定
                .execute()
            author.id // 更新後も既存の ID を返す
        }
    }

    /**
     * 指定された ID の著者を取得する。
     *
     * @param id 検索する著者の ID。
     * @return 見つかった場合は Author オブジェクト、見つからなかった場合は null。
     */
    override fun findById(id: Long): Author? {
        return dsl.selectFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id)) // ID 条件でクエリを絞り込む
            .fetchOne { record -> // 1件のみ取得
                Author(
                    id = record[AUTHORS.ID], // レコードから ID を取得
                    name = record[AUTHORS.NAME], // レコードから名前を取得
                    birthDate = record[AUTHORS.BIRTH_DATE] // レコードから生年月日を取得
                )
            }
    }

    /**
     * 指定された ID の著者が存在するかを確認する。
     *
     * @param id 確認する著者の ID。
     * @return 存在する場合は true、存在しない場合は false。
     */
    override fun existsById(id: Long): Boolean {
        // データベースにレコードが存在するかを確認
        return dsl.fetchExists(dsl.selectFrom(AUTHORS).where(AUTHORS.ID.eq(id)))
    }
}
