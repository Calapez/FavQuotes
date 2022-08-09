package com.brunoponte.favquotes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

// TODO Delete if not has tests

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {

    @Singleton
    @Provides
    @Named("ioDispatcher")
    fun provideIODispatcher() : CoroutineDispatcher = Dispatchers.IO
}