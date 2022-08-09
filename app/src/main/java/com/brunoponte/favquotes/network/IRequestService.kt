package com.brunoponte.favquotes.network

import com.brunoponte.favquotes.network.responses.GetQuoteOfTheDayResponse
import com.brunoponte.favquotes.network.responses.GetQuotesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IRequestService {

    @GET("qotd")
    suspend fun getQuoteOfTheDay() : GetQuoteOfTheDayResponse

    @GET("quotes")
    suspend fun getQuotes(
        @Header("Authorization") authToken: String,
        @Query("page") page: Int,
        @Query("filter") query: String?,
    ) : GetQuotesResponse

}