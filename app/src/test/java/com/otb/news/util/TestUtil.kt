package com.otb.news.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.otb.news.common.CoroutinesDispatcherProvider
import com.otb.news.feature.newslist.NewsModels
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

fun <T> mockRetrofitErrorResponse(): Response<T> {
    val errorResponse = """
            {"code":"404"}
        """.trimIndent()
    val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
    return Response.error(404, errorResponseBody)
}

fun mockArticlesResponse(): List<NewsModels.ArticleResponseItem> {
    return listOf(
        NewsModels.ArticleResponseItem(
            id = 1,
            author = "Stone Cold",
            title = "This is news title",
            description = "I am description",
            url = "https://sample-news.org/hello",
            urlToImage = "https://sample-news.org/image.png",
            publishedAt = Date(),
            content = "I am real content",
            source = NewsModels.SourceResponse("bbc", "BBC News")
        )
    )
}

fun mockNewsApiResponse(): NewsModels.NewsResponse {
    val articles = listOf(
        NewsModels.ArticleResponseItem(
            id = 1,
            author = "Stone Cold",
            title = "This is news title",
            description = "I am description",
            url = "https://sample-news.org/hello",
            urlToImage = "https://sample-news.org/image.png",
            publishedAt = Date(),
            content = "I am real content",
            source = NewsModels.SourceResponse("bbc", "BBC News")
        )
    )
    return NewsModels.NewsResponse("ok", articles.size, articles)
}

fun mockIssueEntity() = NewsModels.ArticleEntity(
    id = 1,
    author = "Stone Cold",
    title = "This is news title",
    description = "I am description",
    imageUrl = "https://sample-news.org/image.png",
    content = "I am real content",
    source = "BBC News",
    publishedAt = "02-Oct-2022"
)

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

@ExperimentalCoroutinesApi
fun mockCoroutinesDispatcherProvider(
    dispatcher: TestCoroutineDispatcher? = null
): CoroutinesDispatcherProvider {
    val sharedTestCoroutineDispatcher = TestCoroutineDispatcher()
    return CoroutinesDispatcherProvider(
        dispatcher ?: sharedTestCoroutineDispatcher,
        dispatcher ?: sharedTestCoroutineDispatcher,
        dispatcher ?: sharedTestCoroutineDispatcher
    )
}
