package com.otb.news.feature.newsdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otb.news.common.CoroutinesDispatcherProvider
import com.otb.news.feature.newslist.NewsModels
import com.otb.news.network.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * Created by Mohit Rajput on 01/10/22.
 */

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) :
    ViewModel() {
    private val _newsLiveData =
        MutableLiveData<ViewState<NewsModels.ArticleEntity>>()
    val newsLiveData: LiveData<ViewState<NewsModels.ArticleEntity>> get() = _newsLiveData

    private val articleEntity by lazy {
        savedStateHandle.get<NewsModels.ArticleEntity>(
            NewsDetailsActivity.ARTICLE_ENTITY
        )!!
    }

    fun fetchNewsDetails() {
        _newsLiveData.value = ViewState.Loading
        viewModelScope.launch(dispatcherProvider.main) {
            _newsLiveData.value = ViewState.Success(articleEntity)
        }
    }
}