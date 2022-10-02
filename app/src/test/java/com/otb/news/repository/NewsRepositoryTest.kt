package com.otb.news.repository

import com.otb.news.db.NewsDao
import com.otb.news.feature.newslist.NewsRepository
import com.otb.news.network.ApiResult
import com.otb.news.network.service.NewsApi
import com.otb.news.util.mockNewsApiResponse
import com.otb.news.util.mockRetrofitErrorResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryTest {

    @MockK
    private lateinit var newsApi: NewsApi

    @MockK
    private lateinit var newsDao: NewsDao

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = true)
    }

    @Test
    fun `success result should be returned on news api success`() = runTest {
        val newsResponse = mockNewsApiResponse()
        coEvery {
            newsApi.fetchNews(any(), any())
        } returns Response.success(newsResponse)

        val repository = NewsRepository(newsApi, newsDao)
        val result = repository.fetchNews(
            country = "in",
            page = 1
        )
        assert(result is ApiResult.Success)
        assertEquals(newsResponse, (result as ApiResult.Success).data)
    }

    @Test
    fun `error result should be returned on news api failure`() = runTest {
        coEvery {
            newsApi.fetchNews(any(), any())
        } returns mockRetrofitErrorResponse()

        val repository = NewsRepository(newsApi, newsDao)
        val result = repository.fetchNews(
            country = "in",
            page = 1
        )
        assert(result is ApiResult.Error)
    }

    @Test
    fun `fetch indian news - local database should be updated`() = runTest {
        val newsResponse = mockNewsApiResponse()
        coEvery {
            newsDao.getAllNews()
        } returns flow { emit(newsResponse.articles) }

        coEvery {
            newsApi.fetchNews(any(), any())
        } returns Response.success(newsResponse)

        val repository = NewsRepository(newsApi, newsDao)
        val result = repository.fetchNews(
            country = "in",
            page = 1
        )

        assert(result is ApiResult.Success)
        assertEquals(newsResponse, (result as ApiResult.Success).data)
        assertEquals(newsResponse.articles, newsDao.getAllNews().first())
    }
}
