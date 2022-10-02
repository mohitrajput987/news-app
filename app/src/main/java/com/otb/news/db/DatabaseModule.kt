package com.otb.news.db

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Created by Mohit Rajput on 01/10/22.
 */

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Provides
    fun providesNewsDao(appDatabase: AppDatabase) = appDatabase.newsDao()
}