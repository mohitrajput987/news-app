package com.otb.news.network.service

import com.otb.news.BuildConfig
import com.otb.news.feature.newslist.NewsModels
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mohit Rajput on 01/10/22.
 */
interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun fetchNews(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("pageSize") perPage: Int = 30,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): Response<NewsModels.NewsResponse>

    @GET("v2/top-headlines")
    suspend fun searchNews(
        @Query("q") keyword: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") perPage: Int = 30,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): Response<NewsModels.NewsResponse>
}