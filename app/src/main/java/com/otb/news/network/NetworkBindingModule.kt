package com.otb.news.network

import com.otb.news.network.interceptor.NetworkStateChecker
import com.otb.news.network.interceptor.NetworkStateCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Mohit Rajput on 01/10/22.
 */

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBindingModule {
    @Binds
    fun bindNetworkStateChecker(networkStateCheckerImpl: NetworkStateCheckerImpl): NetworkStateChecker
}