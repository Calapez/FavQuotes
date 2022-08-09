package com.brunoponte.favquotes.di

import com.brunoponte.favquotes.helpers.Util
import com.brunoponte.favquotes.network.IRequestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideRequestService() : IRequestService {
        return Retrofit.Builder()
            .baseUrl(Util.favQsApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IRequestService::class.java)
    }

}