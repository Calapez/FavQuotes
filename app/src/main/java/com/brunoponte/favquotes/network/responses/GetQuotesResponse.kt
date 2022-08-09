package com.brunoponte.favquotes.network.responses

import com.brunoponte.favquotes.network.dtos.QuoteDto
import com.google.gson.annotations.SerializedName

data class GetQuotesResponse (

    @SerializedName("page")
    val page: Int?,

    @SerializedName("last_page")
    val isLastPage: Boolean?,

    @SerializedName("quotes")
    val quotes: List<QuoteDto>?,
)