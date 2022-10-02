package com.otb.news.feature.newslist

import com.otb.news.db.NewsDao
import com.otb.news.network.ApiResult
import com.otb.news.network.getResult
import com.otb.news.network.service.NewsApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Mohit Rajput on 01/10/22.
 */
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) : NewsListContact.Repository {

    override suspend fun newsFlow(): Flow<List<NewsModels.ArticleResponseItem>> =
        newsDao.getAllNews()

    override suspend fun fetchNews(
        country: String,
        page: Int
    ): ApiResult<NewsModels.NewsResponse> {
        val result = getResult { newsApi.fetchNews(country, page) }
        if (result is ApiResult.Success) {
            newsDao.clear()
            newsDao.insertAll(result.data.articles)
        }
        return result
    }

    override suspend fun searchNews(searchKeyword: String): ApiResult<NewsModels.NewsResponse> {
        val result = getResult { newsApi.searchNews(searchKeyword) }
        if (result is ApiResult.Success) {
            newsDao.clear()
            newsDao.insertAll(result.data.articles)
        }
        return result
    }
}