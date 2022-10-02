package com.otb.news.feature.newslist

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlinx.parcelize.Parcelize

/**
 * Created by Mohit Rajput on 01/10/22.
 */
class NewsModels {
    @Parcelize
    data class ArticleEntity(
        val id: Long,
        val author: String,
        val title: String,
        val description: String,
        val imageUrl: String,
        val content: String,
        val source : String,
        val publishedAt: String
    ) : Parcelable

    @Keep
    data class NewsResponse(
        @SerializedName("status")
        val status: String = "ok",

        @SerializedName("totalResults")
        val totalResults: Int,

        @SerializedName("articles")
        val articles: List<ArticleResponseItem>
    )

    @Keep
    @Entity(tableName = "article")
    data class ArticleResponseItem(
        @SerializedName("id")
        @PrimaryKey(autoGenerate = true)
        val id: Long,

        @SerializedName("author")
        val author: String?,

        @SerializedName("title")
        val title: String,

        @SerializedName("description")
        val description: String?,

        @SerializedName("url")
        val url: String,

        @SerializedName("urlToImage")
        val urlToImage: String?,

        @SerializedName("publishedAt")
        val publishedAt: Date,

        @SerializedName("content")
        val content: String?,

        @SerializedName("source")
        @Embedded
        val source: SourceResponse
    )

    @Keep
    @Entity(tableName = "source")
    data class SourceResponse(
        @SerializedName("id")
        val sourceId: String?,

        @SerializedName("name")
        val name: String?,
    )
}