package com.otb.news.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.otb.news.feature.newslist.NewsModels
import java.util.*

/**
 * Created by Mohit Rajput on 01/10/22.
 */

@Database(
    entities = [NewsModels.ArticleResponseItem::class],
    version = 1
)
@TypeConverters(AppDatabase.DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "news-app.db"
                    ).build()
                }
            }
            return instance!!
        }
    }

    object DateConverter {
        @TypeConverter
        fun toDate(dateLong: Long?): Date? {
            return dateLong?.let { Date(it) }
        }

        @TypeConverter
        fun fromDate(date: Date?): Long? {
            return date?.time
        }
    }
}