package com.otb.news.feature.newsdetails

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.otb.news.common.LoadingSpinner
import com.otb.news.common.base.BaseActivity
import com.otb.news.common.base.DisplaysLoadingSpinner
import com.otb.news.databinding.ActivityNewsDetailsBinding
import com.otb.news.feature.newslist.NewsModels
import com.otb.news.network.ViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailsActivity : BaseActivity<ActivityNewsDetailsBinding>(), DisplaysLoadingSpinner {
    companion object {
        const val ARTICLE_ENTITY = "article_entity"

        fun getIntent(context: Context, articleEntity: NewsModels.ArticleEntity) =
            Intent(context, NewsDetailsActivity::class.java).apply {
                putExtra(ARTICLE_ENTITY, articleEntity)
            }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityNewsDetailsBinding.inflate(layoutInflater)

    private val viewModel by viewModels<NewsDetailsViewModel>()
    override val loadingSpinner by lazy { LoadingSpinner(this) }

    override fun setupView() {
        observeNewsDetails()
        viewModel.fetchNewsDetails()
    }

    private fun observeNewsDetails() {
        viewModel.newsLiveData.observe(this) {
            when (it) {
                is ViewState.Loading -> showLoadingSpinner()
                is ViewState.Success -> showArticleDetails(it.data)
                is ViewState.Error -> showErrorMessage(it.errorMessage)
            }
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        dismissLoadingSpinner()
        binding.tvError.isVisible = true
        binding.tvError.text = errorMessage
    }

    private fun showArticleDetails(articleEntity: NewsModels.ArticleEntity) {
        dismissLoadingSpinner()
        binding.articleEntity = articleEntity
    }
}