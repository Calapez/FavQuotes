package com.brunoponte.favquotes.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.brunoponte.favquotes.cache.daos.QuoteDao
import com.brunoponte.favquotes.cache.entities.QuoteEntity

@Database(
    entities = [QuoteEntity::class],
    version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun quoteDao(): QuoteDao

    companion object{
        const val DATABASE_NAME = "favquotes_db"
    }

}