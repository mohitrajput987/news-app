package com.otb.news.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.otb.news.feature.newslist.NewsModels
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM article ORDER BY publishedAt DESC")
    fun getAllNews(): Flow<List<NewsModels.ArticleResponseItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsModels.ArticleResponseItem>)

    @Query("DELETE FROM article")
    suspend fun clear()
}