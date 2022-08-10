package com.brunoponte.favquotes.repository

import com.brunoponte.favquotes.domainModels.Quote

interface IQuotesRepository {
    suspend fun getQuoteOfDayDay() : Quote?

    suspend fun getQuotes(pageSize: Int, page: Int, query: String, filterType: String?) : List<Quote>

    suspend fun getQuoteById(quoteId: Long) : Quote?
}