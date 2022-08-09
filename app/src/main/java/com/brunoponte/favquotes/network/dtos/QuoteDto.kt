package com.brunoponte.favquotes.network.dtos

import com.google.gson.annotations.SerializedName

data class QuoteDto (

    @SerializedName("id")
    val id: Long,

    @SerializedName("dialogue")
    val isDialogue: Boolean?,

    @SerializedName("private")
    val isPrivate: Boolean?,

    @SerializedName("tags")
    val tags: List<String?>?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("favorites_count")
    val favorites: Int?,

    @SerializedName("upvotes_count")
    val upvotes: Int?,

    @SerializedName("downvotes_count")
    val downvotes: Int?,

    @SerializedName("author")
    val author: String?,

    @SerializedName("author_permalink")
    val authorLink: String?,

    @SerializedName("body")
    val body: String?,
)