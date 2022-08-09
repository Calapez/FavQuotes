package com.brunoponte.favquotes.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo("dialogue")
    var isDialogue: Boolean?,

    @ColumnInfo("private")
    var isPrivate: Boolean?,

    @ColumnInfo("tags")
    var tags: List<String?>? = listOf(),

    @ColumnInfo("url")
    var url: String?,

    @ColumnInfo("favorites_count")
    var favorites: Int?,

    @ColumnInfo("upvotes_count")
    var upvotes: Int?,

    @ColumnInfo("downvotes_count")
    var downvotes: Int?,

    @ColumnInfo("author")
    var author: String?,

    @ColumnInfo("author_permalink")
    var authorLink: String?,

    @ColumnInfo("body")
    var body: String?,
)

