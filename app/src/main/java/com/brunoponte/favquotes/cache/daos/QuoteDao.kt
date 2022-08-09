package com.brunoponte.favquotes.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brunoponte.favquotes.cache.entities.QuoteEntity

@Dao
interface QuoteDao {

    @Query("""
        SELECT * FROM quotes 
        LIMIT :pageSize 
        OFFSET (:pageSize * (:page - 1))
    """)
    suspend fun getQuotes(pageSize: Int, page: Int): List<QuoteEntity>

    @Query("SELECT * FROM quotes WHERE id = :quoteId")
    suspend fun getQuoteById(quoteId: Long): QuoteEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>): LongArray

    @Query("DELETE FROM quotes")
    suspend fun nukeTable()
}