package com.zariya.zariya

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZariyaApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}