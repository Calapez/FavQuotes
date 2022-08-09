package com.brunoponte.favquotes.domainModels

data class Quote(
    val id: Long,
    val isDialogue: Boolean?,
    val isPrivate: Boolean?,
    val tags: List<String?>?,
    val url: String?,
    val favorites: Int?,
    val upvotes: Int?,
    val downvotes: Int?,
    val author: String?,
    val authorLink: String?,
    val body: String?,
)
