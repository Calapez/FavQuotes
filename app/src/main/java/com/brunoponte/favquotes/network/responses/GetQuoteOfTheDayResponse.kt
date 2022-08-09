package com.brunoponte.favquotes.network.responses

import com.brunoponte.favquotes.network.dtos.QuoteDto
import com.google.gson.annotations.SerializedName

data class GetQuoteOfTheDayResponse (

    @SerializedName("qotd_date")
    val date: String?,

    @SerializedName("quote")
    val quote: QuoteDto?,
)