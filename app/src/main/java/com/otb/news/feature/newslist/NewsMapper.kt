package com.otb.news.feature.newslist

import com.otb.news.common.base.Mapper
import com.otb.news.util.DateUtils

/**
 * Created by Mohit Rajput on 01/10/22.
 */
class NewsMapper :
    Mapper<List<NewsModels.ArticleResponseItem>, List<NewsModels.ArticleEntity>> {
    override fun mapFrom(from: List<NewsModels.ArticleResponseItem>): List<NewsModels.ArticleEntity> {
        return from.map {
            NewsModels.ArticleEntity(
                id = it.id,
                author = it.author ?: "Unknown",
                title = it.title,
                description = it.description ?: "",
                imageUrl = it.urlToImage ?: "",
                content = it.content ?: "",
                source = it.source.name ?: "Unknown",
                publishedAt = DateUtils.getFormattedTime(it.publishedAt)
            )
        }
    }
}
