package com.brunoponte.favquotes.di

import com.brunoponte.favquotes.cache.daos.QuoteDao
import com.brunoponte.favquotes.network.IRequestService
import com.brunoponte.favquotes.repository.IQuotesRepository
import com.brunoponte.favquotes.repository.QuotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepoRepository(requestService: IRequestService, quoteDao: QuoteDao) : IQuotesRepository {
        return QuotesRepository(requestService, quoteDao)
    }
}