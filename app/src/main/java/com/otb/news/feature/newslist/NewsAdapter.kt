package com.otb.news.feature.newslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.otb.news.databinding.ItemNewsBinding

/**
 * Created by Mohit Rajput on 01/10/22.
 */
class NewsAdapter(private val onItemClick: (NewsModels.ArticleEntity) -> Unit) :
    ListAdapter<NewsModels.ArticleEntity, NewsAdapter.ViewHolder>(DIFF_UTIL) {
    companion object {
        private val DIFF_UTIL =
            object : DiffUtil.ItemCallback<NewsModels.ArticleEntity>() {
                override fun areItemsTheSame(
                    oldItem: NewsModels.ArticleEntity,
                    newItem: NewsModels.ArticleEntity
                ) = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: NewsModels.ArticleEntity,
                    newItem: NewsModels.ArticleEntity
                ): Boolean {
                    return oldItem.title == newItem.title && oldItem.description ==
                            newItem.description && oldItem.publishedAt == newItem.publishedAt && oldItem.author == newItem.author
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                binding.articleEntity?.let {
                    onItemClick(it)
                }
            }
        }

        fun bind(articleEntity: NewsModels.ArticleEntity) {
            binding.articleEntity = articleEntity
            binding.executePendingBindings()
        }
    }
}