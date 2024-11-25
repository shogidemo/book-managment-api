package com.example.quocard.book_management_api.entity

import java.math.BigDecimal

data class Book(
    val id: Long? = null,
    val title: String,
    val price: BigDecimal,
    private var _published: Boolean = false,
    val authors: List<Author>
) {
    var published: Boolean
        get() = _published
        set(value) {
            if (_published && !value) {
                throw IllegalArgumentException("出版済みの書籍の出版状況を未出版に変更することはできません")
            }
            _published = value
        }

    init {
        if (price < BigDecimal.ZERO) {
            throw IllegalArgumentException("価格は0以上である必要があります")
        }
        if (authors.isEmpty()) {
            throw IllegalArgumentException("書籍は最低1人の著者を持つ必要があります")
        }
    }
}