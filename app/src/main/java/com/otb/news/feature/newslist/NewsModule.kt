package com.otb.news.feature.newslist

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

/**
 * Created by Mohit Rajput on 01/10/22.
 */

@Module
@InstallIn(ActivityRetainedComponent::class)
interface NewsModule {
    @Binds
    fun bindNewsRepository(newsRepository: NewsRepository): NewsListContact.Repository
}