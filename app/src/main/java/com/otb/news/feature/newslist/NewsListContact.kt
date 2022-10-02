package com.otb.news.feature.newslist

import com.otb.news.network.ApiResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by Mohit Rajput on 01/10/22.
 */
class NewsListContact {
    interface Repository {
        suspend fun fetchNews(country: String, page: Int): Flow<ApiResult<NewsModels.NewsResponse>>
        suspend fun searchNews(searchKeyword: String): ApiResult<NewsModels.NewsResponse>
    }
}