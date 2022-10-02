package com.otb.news.repository

import com.otb.news.db.NewsDao
import com.otb.news.network.service.NewsApi
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

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

    }

    @Test
    fun `error result should be returned on news api failure`() = runTest {

    }

    @Test
    fun `get data from local db on network problem`() = runTest {

    }
}
