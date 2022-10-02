package com.otb.news.feature.newslist

import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.otb.news.common.LoadingSpinner
import com.otb.news.common.base.BaseActivity
import com.otb.news.common.base.DisplaysLoadingSpinner
import com.otb.news.common.textChanges
import com.otb.news.databinding.ActivityNewsListBinding
import com.otb.news.feature.newsdetails.NewsDetailsActivity
import com.otb.news.network.ViewState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewsListActivity : BaseActivity<ActivityNewsListBinding>(), DisplaysLoadingSpinner {
    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityNewsListBinding.inflate(layoutInflater)

    private val viewModel by viewModels<NewsListViewModel>()
    private val newsAdapter by lazy {
        NewsAdapter(onItemClick = { articleEntity ->
            onNewsItemClick(articleEntity)
        })
    }
    override val loadingSpinner by lazy { LoadingSpinner(this) }

    override fun setupView() {
        setupRecyclerView()
        setupSearchBar()
        observeArticles()
        viewModel.fetchIndianNews()
    }

    private fun setupSearchBar() {
        binding.etSearchKeyword.textChanges().debounce(500)
            .onEach {
                val searchKeyword = it.toString().trim().lowercase(Locale.ENGLISH)
                if (searchKeyword.isBlank()) {
                    viewModel.fetchIndianNews()
                } else {
                    viewModel.searchNews(searchKeyword)
                }
            }.launchIn(lifecycleScope)
    }

    private fun setupRecyclerView() {
        val verticalLayoutManager = LinearLayoutManager(this@NewsListActivity)
        binding.rvNews.apply {
            layoutManager = verticalLayoutManager
            adapter = newsAdapter
            addItemDecoration(DividerItemDecoration(baseContext, verticalLayoutManager.orientation))
        }
    }

    private fun observeArticles() {
        viewModel.newsLiveData.observe(this) {
            when (it) {
                is ViewState.Loading -> showLoadingSpinner()
                is ViewState.Success -> showArticles(it.data)
                is ViewState.Error -> showErrorMessage(it.errorMessage)
            }
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        dismissLoadingSpinner()
        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showArticles(articleEntities: List<NewsModels.ArticleEntity>) {
        dismissLoadingSpinner()
        newsAdapter.submitList(articleEntities)
    }

    private fun onNewsItemClick(articleEntity: NewsModels.ArticleEntity) {
        startActivity(NewsDetailsActivity.getIntent(this, articleEntity))
    }
}