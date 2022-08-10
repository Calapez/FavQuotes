package com.brunoponte.favquotes.repository

import com.brunoponte.favquotes.cache.daos.QuoteDao
import com.brunoponte.favquotes.cache.entities.QuoteEntityMapper
import com.brunoponte.favquotes.domainModels.Quote
import com.brunoponte.favquotes.network.IRequestService
import com.brunoponte.favquotes.network.dtos.QuoteDtoMapper

class QuotesRepository(
    private val requestService: IRequestService,
    private val quoteDao: QuoteDao
) : IQuotesRepository {

    val authToken = "Bearer f8d23d2189461c7684cb34f67f848153"

    override suspend fun getQuoteOfDayDay(): Quote? {
        return requestService.getQuoteOfTheDay().quote?.let {
            QuoteDtoMapper.mapToDomainModel(it)
        }
    }

    override suspend fun getQuotes(pageSize: Int, page: Int, query: String, filterType: String?) : List<Quote> {

        try {
            val quotes = getQuotesFromNetwork(page, query, filterType)

            // Insert into cache
            if (page == 1) {
                quoteDao.nukeTable()
            }
            quoteDao.insertQuotes(QuoteEntityMapper.toEntityList(quotes))
        } catch (e: Exception) {
            // There was an issue
            e.printStackTrace()
        }

        // Always returns the Repos stored in the cache
        val cachedRepos = quoteDao.getQuotes(pageSize = pageSize, page = page)

        return QuoteEntityMapper.toDomainModelList(cachedRepos)
    }

    override suspend fun getQuoteById(quoteId: Long) : Quote? {
        val quoteEntity = quoteDao.getQuoteById(quoteId)
        return quoteEntity?.let { QuoteEntityMapper.mapToDomainModel(it) }
    }

    private suspend fun getQuotesFromNetwork(page: Int, query: String, filterType: String?) =
        QuoteDtoMapper.toDomainModelList(
            requestService.getQuotes(authToken, page, query.ifEmpty { null }, filterType).quotes?.filter {
                true//it.id != 0L
            } ?: listOf()
        )
}