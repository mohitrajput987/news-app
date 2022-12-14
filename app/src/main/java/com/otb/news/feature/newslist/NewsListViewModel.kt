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
import kotlinx.coroutines.flow.collectLatest
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

    init {
        observeNews()
    }

    private fun observeNews() {
        viewModelScope.launch(dispatcherProvider.io) {
            repository.newsFlow().collectLatest { articles ->
                withContext(dispatcherProvider.main) {
                    val articleEntities = newsMapper.mapFrom(articles)
                    _newsLiveData.value = ViewState.Success(articleEntities)
                }
            }
        }
    }

    fun fetchIndianNews() {
        _newsLiveData.value = ViewState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = repository.fetchNews(country = "in", pageNum)) {
                is ApiResult.Error -> {
                    withContext(dispatcherProvider.main) {
                        _newsLiveData.value = ViewState.Error(result.message)
                    }
                }
            }
        }
    }

    fun searchNews(searchKeyword: String) {
        if (searchKeyword.length < 3) {
            return
        }
        _newsLiveData.value = ViewState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = repository.searchNews(searchKeyword)) {
                is ApiResult.Error -> {
                    withContext(dispatcherProvider.main) {
                        _newsLiveData.value = ViewState.Error(result.message)
                    }
                }
            }
        }
    }
}