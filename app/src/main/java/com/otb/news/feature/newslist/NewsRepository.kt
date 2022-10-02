package com.otb.news.feature.newslist

import com.otb.news.db.NewsDao
import com.otb.news.network.ApiResult
import com.otb.news.network.getResult
import com.otb.news.network.service.NewsApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by Mohit Rajput on 01/10/22.
 */
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) : NewsListContact.Repository {

    override suspend fun fetchNews(
        country: String,
        page: Int
    ): Flow<ApiResult<NewsModels.NewsResponse>> {
        return newsDao.getAllNews().map { articles ->
            ApiResult.Success(
                NewsModels.NewsResponse(
                    totalResults = articles.size,
                    articles = articles
                )
            )
        }.map { localResult ->
            refreshResult(country, page, localResult)
        }
    }

    private suspend fun refreshResult(
        country: String,
        page: Int, localResult: ApiResult.Success<NewsModels.NewsResponse>
    ): ApiResult<NewsModels.NewsResponse> {
        val result = getResult { newsApi.fetchNews(country, page) }
        return if (result is ApiResult.Success) {
            newsDao.clear()
            newsDao.insertAll(result.data.articles)
            result
        } else if (localResult.data.articles.isEmpty())
            result
        else
            localResult
    }
}