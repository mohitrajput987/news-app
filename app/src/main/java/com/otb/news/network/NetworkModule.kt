package com.otb.news.network

import com.otb.news.BuildConfig
import com.otb.news.network.interceptor.NetworkReachabilityInterceptor
import com.otb.news.network.interceptor.NetworkStateChecker
import com.otb.news.network.service.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Mohit Rajput on 01/10/22.
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(networkStateChecker: NetworkStateChecker): OkHttpClient {
        val timeOutInSeconds = 120
        val builder = OkHttpClient.Builder()
            .connectTimeout(timeOutInSeconds.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeOutInSeconds.toLong(), TimeUnit.SECONDS)
            .addInterceptor(NetworkReachabilityInterceptor(networkStateChecker))

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGithubApiService(retrofit: Retrofit) = retrofit.create(NewsApi::class.java)
}