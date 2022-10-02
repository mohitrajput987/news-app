package com.otb.news

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Mohit Rajput on 01/10/22.
 */

@HiltAndroidApp
class NewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}