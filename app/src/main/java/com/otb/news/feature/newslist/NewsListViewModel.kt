package com.otb.news.feature.newslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otb.news.common.CoroutinesDispatcherProvider
import com.otb.news.network.ApiResult
import com.otb.news.network.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Mohit Rajput on 01/10/22.
 */

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: NewsListContact.Repository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) :
    ViewModel() {
    private val _newsLiveData =
        MutableLiveData<ViewState<List<NewsModels.ArticleEntity>>>()
    val newsLiveData: LiveData<ViewState<List<NewsModels.ArticleEntity>>> get() = _newsLiveData

    private val newsMapper = NewsMapper()
    private var pageNum = 1

    fun fetchIndianNews() {
        _newsLiveData.value = ViewState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            repository.fetchNews(country = "in", pageNum).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        withContext(dispatcherProvider.main) {
                            val articleEntities = newsMapper.mapFrom(result.data.articles)
                            _newsLiveData.value = ViewState.Success(articleEntities)
                        }
                    }
                    is ApiResult.Error -> {
                        withContext(dispatcherProvider.main) {
                            _newsLiveData.value = ViewState.Error(result.message)
                        }
                    }
                }
            }

        }
    }
}