package com.brunoponte.favquotes.di

import androidx.room.Room
import com.brunoponte.favquotes.App
import com.brunoponte.favquotes.cache.daos.QuoteDao
import com.brunoponte.favquotes.cache.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideAppDatabase(app: App): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideQuoteDao(db: AppDatabase): QuoteDao {
        return db.quoteDao()
    }

}