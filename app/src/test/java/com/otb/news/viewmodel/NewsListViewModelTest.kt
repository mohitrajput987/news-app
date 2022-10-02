package com.otb.news.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.otb.news.feature.newslist.NewsListContact
import com.otb.news.feature.newslist.NewsListViewModel
import com.otb.news.feature.newslist.NewsMapper
import com.otb.news.network.ApiResult
import com.otb.news.network.ErrorType
import com.otb.news.network.ViewState
import com.otb.news.util.getOrAwaitValue
import com.otb.news.util.mockCoroutinesDispatcherProvider
import com.otb.news.util.mockNewsApiResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Created by Mohit Rajput on 02/10/22.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModelTest {
    @get:Rule
    val liveDataRule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repository: NewsListContact.Repository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = true)
    }

    @Test
    fun `fetch indian news - loading state should be updated`() {
        val response = mockNewsApiResponse()
        coEvery {
            repository.newsFlow()
        } returns flow { ApiResult.Success(response) }

        coEvery {
            repository.fetchNews("in", 1)
        } returns ApiResult.Success(response)

        val articleEntities = NewsMapper().mapFrom(response.articles)
        val viewModel = NewsListViewModel(
            repository,
            mockCoroutinesDispatcherProvider(testCoroutineDispatcher)
        )
        viewModel.newsLiveData.observeForever {

        }

        testCoroutineDispatcher.pauseDispatcher()
        viewModel.fetchIndianNews()

        val viewState = viewModel.newsLiveData.getOrAwaitValue()
        assert(viewState is ViewState.Loading)

        testCoroutineDispatcher.resumeDispatcher()
    }

    @Test
    fun `fetch indian - loading then error`() {
        coEvery {
            repository.fetchNews("in", 1)
        } returns ApiResult.Error("some error message", ErrorType.InvalidData)

        val viewModel = NewsListViewModel(
            repository,
            mockCoroutinesDispatcherProvider(testCoroutineDispatcher)
        )

        testCoroutineDispatcher.pauseDispatcher()
        viewModel.fetchIndianNews()

        val viewState = viewModel.newsLiveData.getOrAwaitValue()
        assert(viewState is ViewState.Loading)

        testCoroutineDispatcher.resumeDispatcher()
        viewModel.newsLiveData.observeForever {
            assertEquals(it, ViewState.Error("some error message"))
        }
    }

    @Test
    fun `search news - loading state should be updated`() {
        val response = mockNewsApiResponse()
        coEvery {
            repository.newsFlow()
        } returns flow { ApiResult.Success(response) }

        coEvery {
            repository.searchNews("keyword")
        } returns ApiResult.Success(response)


        val viewModel = NewsListViewModel(
            repository,
            mockCoroutinesDispatcherProvider(testCoroutineDispatcher)
        )
        viewModel.newsLiveData.observeForever {

        }

        testCoroutineDispatcher.pauseDispatcher()
        viewModel.searchNews("keyword")

        val viewState = viewModel.newsLiveData.getOrAwaitValue()
        assert(viewState is ViewState.Loading)

        testCoroutineDispatcher.resumeDispatcher()
    }

    @Test
    fun `search news - loading then error`() {
        coEvery {
            repository.searchNews("keyword")
        } returns ApiResult.Error("some error message", ErrorType.InvalidData)

        val viewModel = NewsListViewModel(
            repository,
            mockCoroutinesDispatcherProvider(testCoroutineDispatcher)
        )

        testCoroutineDispatcher.pauseDispatcher()
        viewModel.searchNews("keyword")

        val viewState = viewModel.newsLiveData.getOrAwaitValue()
        assert(viewState is ViewState.Loading)

        testCoroutineDispatcher.resumeDispatcher()
        viewModel.newsLiveData.observeForever {
            assertEquals(it, ViewState.Error("some error message"))
        }
    }
}